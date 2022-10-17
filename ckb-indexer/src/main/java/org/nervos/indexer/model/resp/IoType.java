package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public enum IoType {
  @SerializedName("input")
  INPUT,
  @SerializedName("output")
  OUTPUT
}
