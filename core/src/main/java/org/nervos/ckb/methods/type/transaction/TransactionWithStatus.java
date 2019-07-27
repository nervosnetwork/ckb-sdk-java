package org.nervos.ckb.methods.type.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionWithStatus {

  @JsonProperty("tx_status")
  public TxStatus txStatus;

  public Transaction transaction;

  public static class TxStatus {

    // pending / proposed / committed
    public String status;

    @JsonProperty("block_hash")
    public String blockHash;
  }
}
