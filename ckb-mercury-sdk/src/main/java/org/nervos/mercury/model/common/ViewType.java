package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public enum ViewType {
  @SerializedName("Native")
  NATIVE,
  @SerializedName("DoubleEntry")
  DOUBLE_ENTRY,
}
