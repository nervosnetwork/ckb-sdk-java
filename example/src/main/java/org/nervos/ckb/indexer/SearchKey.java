package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;
import java.util.List;
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

    @SerializedName("args_len")
    public String argsLen;

    @SerializedName("output_data_len_range")
    public List<String> outputDataLenRange;

    @SerializedName("output_capacity_range")
    public List<String> outputCapacityRange;

    @SerializedName("block_range")
    public List<String> blockRange;

    public Filter(Script script) {
      this.script = script;
    }

    public Filter(
        Script script,
        String argsLen,
        List<String> outputDataLenRange,
        List<String> outputCapacityRange,
        List<String> blockRange) {
      this.script = script;
      this.argsLen = argsLen;
      this.outputDataLenRange = outputDataLenRange;
      this.outputCapacityRange = outputCapacityRange;
      this.blockRange = blockRange;
    }
  }
}
