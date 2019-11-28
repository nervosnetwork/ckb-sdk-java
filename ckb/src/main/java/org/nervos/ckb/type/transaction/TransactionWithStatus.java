package org.nervos.ckb.type.transaction;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionWithStatus {
  public static final String PENDING = "pending";
  public static final String PROPOSED = "proposed";
  public static final String COMMITTED = "committed";

  @SerializedName("tx_status")
  public TxStatus txStatus;

  public Transaction transaction;

  public static class TxStatus {

    // pending / proposed / committed
    public String status;

    @SerializedName("block_hash")
    public String blockHash;
  }
}
