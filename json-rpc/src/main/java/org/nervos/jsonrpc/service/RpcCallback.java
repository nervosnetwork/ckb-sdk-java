package org.nervos.jsonrpc.service;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public interface RpcCallback<T> {

  void onFailure(String errorMessage);

  void onResponse(T response);
}
