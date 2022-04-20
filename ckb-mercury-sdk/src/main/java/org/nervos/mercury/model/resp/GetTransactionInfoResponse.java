package org.nervos.mercury.model.resp;

import org.nervos.ckb.type.transaction.TransactionWithStatus;

public class GetTransactionInfoResponse {
  public TransactionInfoResponse transaction;
  public TransactionWithStatus.Status status;
  public Integer rejectReason;
}
