package org.nervos.indexer.model;

import com.google.gson.annotations.SerializedName;

public enum Order {
  @SerializedName("asc")
  ASC,
  @SerializedName("desc")
  DESC,
}
