package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

public enum ScriptType {
  @SerializedName("LOCK")
  LOCK,
  @SerializedName("TYPE")
  TYPE
}
