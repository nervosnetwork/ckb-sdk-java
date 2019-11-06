package org.nervos.ckb.type.transaction;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionPoint {
  @SerializedName("block_number")
  public String blockNumber;

  @SerializedName("tx_hash")
  public String txHash;

  public String index;
}
