package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-04-28. Copyright © 2019 Nervos Foundation. All rights reserved. */
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
