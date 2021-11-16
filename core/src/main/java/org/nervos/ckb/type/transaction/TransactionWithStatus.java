package org.nervos.ckb.type.transaction;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionWithStatus {

  @SerializedName("tx_status")
  public TxStatus txStatus;

  public Transaction transaction;

  public static class TxStatus {

    // pending / proposed / committed
    public String status;

    @SerializedName("block_hash")
    public String blockHash;
  }

  public enum Status {
    PENDING("pending"),
    PROPOSED("proposed"),
    COMMITTED("committed"),
    UNKNOWN("unknown"),
    REJECTED("rejected");

    private final String value;

    Status(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
