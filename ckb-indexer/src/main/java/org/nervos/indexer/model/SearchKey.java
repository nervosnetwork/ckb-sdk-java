package org.nervos.indexer.model;

import com.google.gson.annotations.SerializedName;

public class SearchKey {
  public Script script;

  @SerializedName("script_type")
  public ScriptType scriptType;

  public Filter filter;
}
