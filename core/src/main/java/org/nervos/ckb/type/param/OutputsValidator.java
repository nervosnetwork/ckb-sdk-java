package org.nervos.ckb.type.param;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public enum OutputsValidator {
  @SerializedName("well_known_scripts_only")
  WELL_KNOWN_SCRIPTS_ONLY,
  @SerializedName("passthrough")
  PASSTHROUGH;
}
