package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public enum ExtraFilterType {
  @SerializedName("DAO")
  DAO,
  @SerializedName("CellBase")
  CELL_BASE,
}
