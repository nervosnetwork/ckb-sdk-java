package org.nervos.mercury.model.resp;

import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.type.transaction.TransactionWithStatus;

public class TransactionWithRichStatus {
  public Transaction transaction;
  public TxRichStatus txStatus;

  public class TxRichStatus {
    public TransactionWithStatus.Status status;
    public byte[] blockHash;
    public String reason;
    public long timestamp;
  }
}
