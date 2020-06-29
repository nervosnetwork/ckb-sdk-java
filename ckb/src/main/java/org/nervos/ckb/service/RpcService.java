package org.nervos.ckb.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
class RpcService {

  private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
  private static AtomicLong nextId = new AtomicLong(0);

  private OkHttpClient client;
  private String url;
  private Gson gson;

  RpcService(String nodeUrl, boolean isDebug) {
    url = nodeUrl;
    if (isDebug) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.level(HttpLoggingInterceptor.Level.BODY);
      client = new OkHttpClient.Builder().addInterceptor(logging).build();
    } else {
      client = new OkHttpClient();
    }
    gson = new Gson();
  }

  <T> T post(@NotNull String method, List params, Type cls) throws IOException {
    RequestParams requestParams = new RequestParams(method, params);
    RequestBody body = RequestBody.create(gson.toJson(requestParams), JSON_MEDIA_TYPE);
    Request request = new Request.Builder().url(url).post(body).build();
    Response response = client.newCall(request).execute();
    if (response.isSuccessful()) {
      String responseBody = Objects.requireNonNull(response.body()).string();
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

  <T> void postAsync(
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
                if (response.isSuccessful()) {
                  String responseBody = Objects.requireNonNull(response.body()).string();
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

  List<RpcResponse> batchPost(List<List> requests) throws IOException {
    List<RequestParams> paramsList = new ArrayList<>();
    for (List request : requests) {
      if (request.size() == 0 || !(request.get(0) instanceof String)) {
        throw new IOException("RPC method name must be a non-null string");
      }
      paramsList.add(
          new RequestParams(request.get(0).toString(), request.subList(1, request.size())));
    }
    RequestBody body = RequestBody.create(gson.toJson(paramsList), JSON_MEDIA_TYPE);
    Request request = new Request.Builder().url(url).post(body).build();
    Response response = client.newCall(request).execute();
    if (response.isSuccessful()) {
      String responseBody = Objects.requireNonNull(response.body()).string();
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
