package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class OutPoint {

  @SerializedName("tx_hash")
  public byte[] txHash;

  public int index;

  public OutPoint() {}

  public OutPoint(byte[] txHash, int index) {
    this.txHash = txHash;
    this.index = index;
  }
}
