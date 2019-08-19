package org.nervos.ckb.methods.type.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nervos.ckb.methods.type.Script;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutput {
  public String capacity;
  public Script type;
  public Script lock;

  @JsonProperty("data_hash")
  public String dataHash;

  public CellOutput() {}

  public CellOutput(String capacity, String dataHash, Script lock) {
    this.capacity = capacity;
    this.dataHash = dataHash;
    this.lock = lock;
  }
}
