package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutputWithOutPoint {
  @SerializedName("block_hash")
  public String blockHash;

  public String capacity;

  public Script lock;

  @SerializedName("out_point")
  public OutPoint outPoint;

  public CellOutputWithOutPoint() {}
}
