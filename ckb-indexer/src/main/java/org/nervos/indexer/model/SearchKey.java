package org.nervos.indexer.model;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

public class SearchKey {
  public Script script;

  @SerializedName("script_type")
  public ScriptType scriptType;

  public Filter filter;
}
