package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

public class SearchKey {
  public Script script;

  @SerializedName("script_type")
  public ScriptType scriptType;

  public Filter filter;

  public SearchKey(Script script, ScriptType scriptType, Filter filter) {
    this.script = script;
    this.scriptType = scriptType;
    this.filter = filter;
  }

  public SearchKey(Script script, ScriptType scriptType) {
    this.script = script;
    this.scriptType = scriptType;
    this.filter = null;
  }

  public SearchKey(Script script) {
    this.script = script;
    this.scriptType = ScriptType.LOCK;
    this.filter = null;
  }

  public static class Filter {
    public Script script;

    @SerializedName("output_data_len_range")
    public int[] outputDataLenRange;

    @SerializedName("output_capacity_range")
    public int[] outputCapacityRange;

    @SerializedName("block_range")
    public int[] blockRange;

    public Filter(Script script) {
      this.script = script;
    }

    public Filter(
        Script script, int[] outputDataLenRange, int[] outputCapacityRange, int[] blockRange) {
      this.script = script;
      this.outputDataLenRange = outputDataLenRange;
      this.outputCapacityRange = outputCapacityRange;
      this.blockRange = blockRange;
    }
  }
}
