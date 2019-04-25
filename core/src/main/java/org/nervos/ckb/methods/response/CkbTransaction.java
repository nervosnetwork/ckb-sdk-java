package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.Transaction;

/** Created by duanyytop on 2019-01-07. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbTransaction extends Response<Transaction> {

  public Transaction getTransaction() {
    return result;
  }
}
