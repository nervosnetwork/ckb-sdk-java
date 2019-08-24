package org.nervos.ckb.methods.type.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nervos.ckb.methods.type.OutPoint;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellDep {

  public static final String CODE = "code";
  public static final String DEP_GROUP = "dep_group";

  @JsonProperty("out_point")
  public OutPoint outPoint;

  @JsonProperty("dep_type")
  public String depType;

  public CellDep() {}

  public CellDep(OutPoint outPoint, String depType) {
    this.outPoint = outPoint;
    this.depType = depType;
  }
}
