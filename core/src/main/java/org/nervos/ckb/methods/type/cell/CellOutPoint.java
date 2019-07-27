package org.nervos.ckb.methods.type.cell;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellOutPoint {

  @JsonProperty("tx_hash")
  public String txHash;

  public String index;

  public CellOutPoint() {}

  public CellOutPoint(String txHash, String index) {
    this.txHash = txHash;
    this.index = index;
  }
}
