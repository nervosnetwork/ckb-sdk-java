package org.nervos.indexer.model;

import com.google.gson.annotations.SerializedName;

public enum ScriptSearchMode {
  // search script with prefix
  @SerializedName("prefix")
  Prefix,
  // search script with exact match
  @SerializedName("exact")
  Exact,
  ;
}
