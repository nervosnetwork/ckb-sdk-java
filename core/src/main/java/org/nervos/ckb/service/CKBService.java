package org.nervos.ckb.service;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public interface CKBService extends CKBApi {

  /**
   * Construct a new apiService instance.
   *
   * @param apiService api service instance - i.e. HTTP or IPC
   * @return new apiService instance
   */
  static CKBService build(APIService apiService) {
    return new JsonRpcCKBApiImpl(apiService);
  }
}
