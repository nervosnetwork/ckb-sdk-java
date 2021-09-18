package org.nervos.indexer;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.Script;

public class SearchKey {
  public Script script;

  @SerializedName("script_type")
  public ScriptType scriptType;

  public Filter filter;
}
