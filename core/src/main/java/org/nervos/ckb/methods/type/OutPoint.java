package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class OutPoint {

  @JsonProperty("tx_hash")
  public String txHash;

  public String index;

  public OutPoint() {}

  public OutPoint(String txHash, String index) {
    this.txHash = txHash;
    this.index = index;
  }
}
