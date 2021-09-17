package org.nervos.mercury.model.resp;

import org.nervos.ckb.type.transaction.TransactionWithStatus;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class GetTransactionInfoResponse {

  public TransactionInfoResponse transaction;

  public TransactionWithStatus.Status status;

  public Integer reject_reason;
}
