package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

public enum ScriptType {
  @SerializedName("lock")
  LOCK,
  @SerializedName("type")
  TYPE
}
