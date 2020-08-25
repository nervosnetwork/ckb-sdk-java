package org.nervos.ckb.service;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
interface RpcCallback<T> {

  void onFailure(String errorMessage);

  void onResponse(T response);
}
