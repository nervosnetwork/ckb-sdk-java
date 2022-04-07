package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;

public enum Source {
  @SerializedName("free")
  FREE,
  @SerializedName("Claimable")
  CLAIMABLE,
}
