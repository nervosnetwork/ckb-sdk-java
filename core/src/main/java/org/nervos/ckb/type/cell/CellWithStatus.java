package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellWithStatus {

  public CellInfo cell;
  public Status status;

  public static class CellInfo {
    public CellData data;
    public CellOutput output;

    public static class CellData {
      public byte[] content;
      public byte[] hash;
    }
  }

  public enum Status {
    @SerializedName("live")
    LIVE,
    @SerializedName("dead")
    DEAD,
    @SerializedName("unknown")
    UNKNOWN;
  }
}
