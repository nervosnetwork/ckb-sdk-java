package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;

public enum Mode {
  @SerializedName("HoldByFrom")
  HOLD_BY_FROM,
  @SerializedName("HoldByTo")
  HOLD_BY_TO,
}
