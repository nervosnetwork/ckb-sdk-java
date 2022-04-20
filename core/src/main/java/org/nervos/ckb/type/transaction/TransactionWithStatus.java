package org.nervos.ckb.type.transaction;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionWithStatus {
  public TxStatus txStatus;
  public Transaction transaction;

  public static class TxStatus {
    public Status status;
    public byte[] blockHash;
  }

  public enum Status {
    @SerializedName("pending")
    PENDING,
    @SerializedName("proposed")
    PROPOSED,
    @SerializedName("committed")
    COMMITTED,
    @SerializedName("unknown")
    UNKNOWN,
    @SerializedName("rejected")
    REJECTED;
  }
}
