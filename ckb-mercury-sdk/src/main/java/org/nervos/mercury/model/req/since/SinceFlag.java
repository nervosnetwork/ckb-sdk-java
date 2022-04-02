package org.nervos.mercury.model.req.since;

import com.google.gson.annotations.SerializedName;

public enum SinceFlag {
  @SerializedName("Relative")
  RELATIVE,
  @SerializedName("Absolute")
  ABSOLUTE,
}
