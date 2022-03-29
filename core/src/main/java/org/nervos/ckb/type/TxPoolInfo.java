package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TxPoolInfo {

  public int orphan;
  public int pending;
  public int proposed;

  @SerializedName("last_txs_updated_at")
  public long lastTxsUpdatedAt;

  @SerializedName("total_tx_cycles")
  public long totalTxCycles;

  @SerializedName("total_tx_size")
  public int totalTxSize;

  @SerializedName("min_fee_rate")
  public BigInteger minFeeRate;

  @SerializedName("tip_hash")
  public byte[] tipHash;

  @SerializedName("tip_number")
  public int tipNumber;
}
