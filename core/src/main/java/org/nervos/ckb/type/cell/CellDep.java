package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellDep {
  @SerializedName("out_point")
  public OutPoint outPoint;

  @SerializedName("dep_type")
  public DepType depType;

  public CellDep() {}

  public CellDep(OutPoint outPoint, DepType depType) {
    this.outPoint = outPoint;
    this.depType = depType;
  }

  public CellDep(OutPoint outPoint) {
    this.outPoint = outPoint;
    this.depType = DepType.CODE;
  }

  public enum DepType {
    CODE,
    DEP_GROUP;
  }
}
