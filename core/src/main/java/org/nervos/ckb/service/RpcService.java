package org.nervos.ckb.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class RpcService {

  private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
  private static AtomicLong nextId = new AtomicLong(0);

  private OkHttpClient client;
  private String url;
  private Gson gson;

  public RpcService(String rpcUrl, boolean isDebug) {
    url = rpcUrl;
    if (isDebug) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.level(HttpLoggingInterceptor.Level.BODY);
      client =
          new OkHttpClient.Builder().addInterceptor(logging).retryOnConnectionFailure(true).build();

    } else {
      client =
          new OkHttpClient.Builder()
              .connectTimeout(180, TimeUnit.SECONDS)
              .readTimeout(180, TimeUnit.SECONDS)
              .retryOnConnectionFailure(true)
              .build();
    }
    gson = new Gson();
  }

  public <T> T post(@NotNull String method, List params, Type cls) throws IOException {
    return post(method, params, cls, gson);
  }

  public <T> T post(@NotNull String method, List params, Type cls, Gson gson) throws IOException {
    RequestParams requestParams = new RequestParams(method, params);
    RequestBody body = RequestBody.create(gson.toJson(requestParams), JSON_MEDIA_TYPE);
    Request request = new Request.Builder().url(url).post(body).build();
    Response response = client.newCall(request).execute();
    String responseBody = Objects.requireNonNull(response.body()).string();
    response.close();
    if (response.isSuccessful()) {
      RpcResponse rpcResponse =
          gson.fromJson(responseBody, new TypeToken<RpcResponse>() {}.getType());

      if (rpcResponse.error != null) {
        throw new IOException(
            "RpcService method " + method + " error " + gson.toJson(rpcResponse.error));
      }

      JsonElement jsonElement =
          new JsonParser().parse(responseBody).getAsJsonObject().get("result");
      if (jsonElement.isJsonObject()) {
        return gson.fromJson(jsonElement.getAsJsonObject(), cls);
      }
      return gson.fromJson(jsonElement, cls);
    } else {
      throw new IOException("RpcService method " + method + " error code " + response.code());
    }
  }

  public <T> void postAsync(
      @NotNull String method, List params, @NotNull Type cls, @NotNull RpcCallback<T> callback) {
    RequestParams requestParams = new RequestParams(method, params);
    RequestBody body = RequestBody.create(gson.toJson(requestParams), JSON_MEDIA_TYPE);
    Request request = new Request.Builder().url(url).post(body).build();
    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailure(e.getMessage());
              }

              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                String responseBody = Objects.requireNonNull(response.body()).string();
                response.close();
                if (response.isSuccessful()) {
                  RpcResponse<T> rpcResponse =
                      gson.fromJson(responseBody, new TypeToken<RpcResponse<T>>() {}.getType());
                  if (rpcResponse.error != null) {
                    throw new IOException(
                        "RpcService method " + method + " error " + gson.toJson(rpcResponse.error));
                  }
                  JsonElement jsonElement =
                      new JsonParser().parse(responseBody).getAsJsonObject().get("result");
                  if (jsonElement.isJsonObject()) {
                    callback.onResponse(gson.fromJson(jsonElement.getAsJsonObject(), cls));
                  }
                  callback.onResponse(gson.fromJson(jsonElement, cls));
                } else {
                  throw new IOException(
                      "RpcService method " + method + " error code " + response.code());
                }
              }
            });
  }

  public List<RpcResponse> batchPost(List<List> requests) throws IOException {
    List<RequestParams> paramsList = new ArrayList<>();
    for (List request : requests) {
      if (request.size() == 0 || !(request.get(0) instanceof String)) {
        throw new IOException("RPC method name must be a non-null string");
      }
      for (int i = 1; i < request.size(); i++) {
        if (Numeric.isIntegerValue(request.get(i).toString())) {
          request.set(i, Numeric.toHexString(request.get(i).toString()));
        }
      }
      paramsList.add(
          new RequestParams(request.get(0).toString(), request.subList(1, request.size())));
    }
    RequestBody body = RequestBody.create(gson.toJson(paramsList), JSON_MEDIA_TYPE);
    Request request = new Request.Builder().url(url).post(body).build();
    Response response = client.newCall(request).execute();
    String responseBody = Objects.requireNonNull(response.body()).string();
    response.close();
    if (response.isSuccessful()) {
      return gson.fromJson(responseBody, new TypeToken<List<RpcResponse>>() {}.getType());
    } else {
      throw new IOException("RpcService error code " + response.code());
    }
  }

  static class RequestParams {
    String jsonrpc = "2.0";
    String method;
    List params;
    long id;

    public RequestParams(String method, List params) {
      this.method = method;
      this.params = params;
      this.id = nextId.getAndIncrement();
    }
  }
}
