package org.nervos.ckb.methods.type.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nervos.ckb.methods.type.OutPoint;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellDep {

  @JsonProperty("out_point")
  public OutPoint outPoint;

  @JsonProperty("is_dep_group")
  public boolean isDepGroup;

  public CellDep() {}

  public CellDep(OutPoint outPoint, boolean isDepGroup) {
    this.outPoint = outPoint;
    this.isDepGroup = isDepGroup;
  }
}
