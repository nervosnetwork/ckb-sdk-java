package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class OutPoint {

  @SerializedName("tx_hash")
  public String txHash;

  public String index;

  public OutPoint() {}

  public OutPoint(String txHash, String index) {
    this.txHash = txHash;
    this.index = index;
  }
}
