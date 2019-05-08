package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.TxPoolInfo;

/** Created by duanyytop on 2019-05-08. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbTxPoolInfo extends Response<TxPoolInfo> {

  public TxPoolInfo getTxPoolInfo() {
    return result;
  }
}
