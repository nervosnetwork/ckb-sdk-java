package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

public enum OutputsValidator {
  @SerializedName("well_known_scripts_only")
  WELL_KNOWN_SCRIPTS_ONLY,
  @SerializedName("passthrough")
  PASSTHROUGH
}
