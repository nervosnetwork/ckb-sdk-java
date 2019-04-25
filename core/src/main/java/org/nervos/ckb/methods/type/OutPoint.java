package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-01-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class OutPoint {

  @JsonProperty("tx_hash")
  public String txHash;

  public int index;

  public OutPoint() {}

  public OutPoint(String txHash, int index) {
    this.txHash = txHash;
    this.index = index;
  }
}
