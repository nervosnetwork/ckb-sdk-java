package org.nervos.ckb.methods.type.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.Script;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutputWithOutPoint {
  @JsonProperty("block_hash")
  public OutPoint blockHash;

  public String capacity;

  public Script lock;

  @JsonProperty("out_point")
  public OutPoint outPoint;
}
