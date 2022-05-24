package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public class ExtraFilter {
  public Type type;
  public DaoInfo value;

  public enum Type {
    @SerializedName("Dao")
    DAO,
    @SerializedName("CellBase")
    CELL_BASE,
    @SerializedName("Freeze")
    FREEZE,
  }
}
