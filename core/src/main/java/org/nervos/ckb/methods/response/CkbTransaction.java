package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.transaction.TransactionWithStatus;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbTransaction extends Response<TransactionWithStatus> {

  public TransactionWithStatus getTransaction() {
    return result;
  }
}
