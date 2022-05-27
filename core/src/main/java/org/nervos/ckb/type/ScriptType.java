package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

public enum ScriptType {
  @SerializedName(value = "lock", alternate = {"Lock"})
  LOCK,
  @SerializedName(value = "type", alternate = {"Type"})
  TYPE
}
