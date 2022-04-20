package org.nervos.ckb.service;

public interface RpcCallback<T> {

  void onFailure(String errorMessage);

  void onResponse(T response);
}
