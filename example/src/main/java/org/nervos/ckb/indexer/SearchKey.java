package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.Script;

public class SearchKey {
  public Script script;

  @SerializedName("script_type")
  public String scriptType;

  public Filter filter;

  public SearchKey(Script script, String scriptType, Filter filter) {
    this.script = script;
    this.scriptType = scriptType;
    this.filter = filter;
  }

  public SearchKey(Script script, String scriptType) {
    this.script = script;
    this.scriptType = scriptType;
    this.filter = null;
  }

  public SearchKey(Script script) {
    this.script = script;
    this.scriptType = "lock";
    this.filter = null;
  }

  public static class Filter {
    public Script script;

    public Filter(Script script) {
      this.script = script;
    }
  }
}
