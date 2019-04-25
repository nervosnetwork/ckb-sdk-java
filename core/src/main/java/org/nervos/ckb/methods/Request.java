package org.nervos.ckb.methods;

import io.reactivex.Flowable;
import org.nervos.ckb.service.APIService;
import org.nervos.ckb.service.RemoteCall;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/** Created by duanyytop on 2018-12-20. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Request<S, T extends Response> {
  private static AtomicLong nextId = new AtomicLong(0);

  public String jsonrpc = "2.0";
  public String method;
  public List<S> params;
  public long id;

  private APIService apiService;

  private Class<T> responseType;

  public Request() {}

  public Request(String method, List<S> params, APIService apiService, Class<T> type) {
    this.method = method;
    this.params = params;
    this.id = nextId.getAndIncrement();
    this.apiService = apiService;
    this.responseType = type;
  }

  public T send() throws IOException {
    return apiService.send(this, responseType);
  }

  public Future<T> sendAsync() {
    return apiService.sendAsync(this, responseType);
  }

  public Flowable<T> flowable() {
    return new RemoteCall<T>(this::send).flowable();
  }
}
