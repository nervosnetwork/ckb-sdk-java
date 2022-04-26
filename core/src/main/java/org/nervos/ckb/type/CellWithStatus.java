package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

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
    UNKNOWN
  }
}
