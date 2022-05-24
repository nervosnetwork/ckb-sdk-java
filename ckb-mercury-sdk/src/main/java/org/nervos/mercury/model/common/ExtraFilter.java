package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public class ExtraFilter {
  public FilterType type;
  public DaoInfo value;

  enum FilterType {
    @SerializedName("Dao")
    DAO,
    @SerializedName("CellBase")
    CELL_BASE,
    @SerializedName("Freeze")
    FREEZE,
  }
}
