package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellDep {

  public static final String CODE = "code";
  public static final String DEP_GROUP = "dep_group";

  @SerializedName("out_point")
  public OutPoint outPoint;

  @SerializedName("dep_type")
  public String depType;

  public CellDep() {}

  public CellDep(OutPoint outPoint, String depType) {
    this.outPoint = outPoint;
    this.depType = depType;
  }
}
