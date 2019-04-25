package org.nervos.ckb.service;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.nervos.ckb.exceptions.ClientConnectionException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/** HTTP implementation of our services API. */
public class HttpService extends APIServiceImpl {

  private static final MediaType JSON_MEDIA_TYPE =
      MediaType.parse("application/json; charset=utf-8");

  private static final String DEFAULT_URL = "http://localhost:8114/";

  private static boolean debug = false;

  private final OkHttpClient httpClient;

  private final String url;

  private HashMap<String, String> headers = new HashMap<>();

  public HttpService(String url, OkHttpClient httpClient) {
    super();
    this.url = url;
    this.httpClient = httpClient;
  }

  public HttpService(OkHttpClient httpClient) {
    this(DEFAULT_URL, httpClient);
  }

  public HttpService(String url) {
    this(url, createOkHttpClient());
  }

  public HttpService() {
    this(DEFAULT_URL);
  }

  private static OkHttpClient createOkHttpClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    configureLogging(builder);
    return builder.build();
  }

  private static void configureLogging(OkHttpClient.Builder builder) {
    if (debug) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.addInterceptor(logging);
    }
  }

  @Override
  protected Reader performIO(String request) throws IOException {

    RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, request);
    Headers headers = buildHeaders();

    okhttp3.Request httpRequest =
        new okhttp3.Request.Builder().url(url).headers(headers).post(requestBody).build();

    okhttp3.Response response = httpClient.newCall(httpRequest).execute();
    if (response.isSuccessful()) {
      ResponseBody responseBody = response.body();
      if (responseBody != null) {
        return buildReader(responseBody);
      } else {
        return null;
      }
    } else {
      throw new ClientConnectionException("Invalid response received: " + response.body());
    }
  }

  private Reader buildReader(ResponseBody responseBody) throws IOException {
    return new InputStreamReader(responseBody.byteStream());
  }

  private Headers buildHeaders() {
    return Headers.of(headers);
  }

  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  public void addHeaders(Map<String, String> headersToAdd) {
    headers.putAll(headersToAdd);
  }

  public HashMap<String, String> getHeaders() {
    return headers;
  }

  public static void setDebug(boolean isDebug) {
    debug = isDebug;
  }
}
