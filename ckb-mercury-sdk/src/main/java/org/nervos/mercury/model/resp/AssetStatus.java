package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;

public enum AssetStatus {
  @SerializedName("Claimable")
  CLAIMABLE,
  @SerializedName("Fixed")
  FIXED
}
