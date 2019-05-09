package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-01-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class OutPoint {

  @JsonProperty("block_hash")
  public String blockHash;

  public CellOutPoint cell;

  public OutPoint() {}

  public OutPoint(CellOutPoint cellOutPoint) {
    this.cell = cellOutPoint;
  }

  public OutPoint(String blockHash, CellOutPoint cellOutPoint) {
    this.blockHash = blockHash;
    this.cell = cellOutPoint;
  }
}
