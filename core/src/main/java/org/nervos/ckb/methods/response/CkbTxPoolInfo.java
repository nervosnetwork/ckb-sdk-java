package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.TxPoolInfo;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbTxPoolInfo extends Response<TxPoolInfo> {

  public TxPoolInfo getTxPoolInfo() {
    return result;
  }
}
