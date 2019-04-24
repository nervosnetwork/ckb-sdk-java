package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Created by duanyytop on 2019-01-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellInput {

  @JsonProperty("tx_hash")
  public String txHash;

  public long index;

  public long since;
  public List<String> args;

  public CellInput() {}

  public CellInput(String txHash, long index, long since, List<String> args) {
    this.txHash = txHash;
    this.index = index;
    this.since = since;
    this.args = args;
  }
}
