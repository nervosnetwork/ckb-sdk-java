package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.Script;

public class SearchKey {
  public Script script;
  @SerializedName("script_type")
  public String scriptType;

  public SearchKey(Script script, String scriptType) {
    this.script = script;
    this.scriptType = scriptType;
  }
  public SearchKey(Script script) {
    this.script = script;
    this.scriptType = "lock";
  }
}
