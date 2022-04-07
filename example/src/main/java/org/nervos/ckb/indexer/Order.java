package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;

public enum Order {
  @SerializedName("asc")
  ASC,
  @SerializedName("desc")
  DESC,
}
