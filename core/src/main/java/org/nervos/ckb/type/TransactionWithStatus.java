package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

public class TransactionWithStatus {
  public TxStatus txStatus;
  public Transaction transaction;
  public Long cycles;
  public Long timeAddedToPool;

  public static class TxStatus {
    public Status status;
    public byte[] blockHash;
  }

  public enum Status {
    @SerializedName(value = "pending", alternate = "Pending")
    PENDING,
    @SerializedName(value = "proposed", alternate = "Proposed")
    PROPOSED,
    @SerializedName(value = "committed", alternate = "Committed")
    COMMITTED,
    @SerializedName(value = "unknown", alternate = "Unknown")
    UNKNOWN,
    @SerializedName(value = "rejected", alternate = "Rejected")
    REJECTED
  }
}
