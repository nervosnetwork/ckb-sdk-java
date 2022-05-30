package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public class ExtraFilter {
  public Type type;
  public DaoInfo value;

  public enum Type {
    @SerializedName("Dao")
    DAO,
    @SerializedName("Cellbase")
    CELL_BASE,
    @SerializedName("Frozen")
    FROZEN,
  }
}
