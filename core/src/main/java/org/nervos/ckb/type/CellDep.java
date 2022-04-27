package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

public class CellDep {
  public OutPoint outPoint;
  public DepType depType;

  public CellDep() {
  }

  public CellDep(OutPoint outPoint, DepType depType) {
    this.outPoint = outPoint;
    this.depType = depType;
  }

  public CellDep(OutPoint outPoint) {
    this.outPoint = outPoint;
    this.depType = DepType.CODE;
  }

  public org.nervos.ckb.type.concrete.CellDep pack() {
    return org.nervos.ckb.type.concrete.CellDep.builder()
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
