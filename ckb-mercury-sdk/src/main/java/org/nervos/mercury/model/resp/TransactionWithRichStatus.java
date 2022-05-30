package org.nervos.mercury.model.resp;

import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.type.TransactionWithStatus;

public class TransactionWithRichStatus {
  public Transaction transaction;
  public TxRichStatus txStatus;

  public static class TxRichStatus {
    public TransactionWithStatus.Status status;
    public byte[] blockHash;
    public String reason;
    public long timestamp;
  }
}
