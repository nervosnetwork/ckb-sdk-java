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

  public org.nervos.ckb.newtype.concrete.CellDep pack() {
    return org.nervos.ckb.newtype.concrete.CellDep.builder()
            .setOutPoint(outPoint.pack())
            .setDepType(depType.pack())
            .build();
  }

  public enum DepType {
    @SerializedName("code")
    CODE(0x00),
    @SerializedName("dep_group")
    DEP_GROUP(0x01);

    private byte value;

    DepType(int value) {
      this.value = (byte) value;
    }

    public byte pack() {
      return value;
    }
  }
}
