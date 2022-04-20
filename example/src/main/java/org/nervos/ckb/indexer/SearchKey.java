package org.nervos.ckb.indexer;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

public class SearchKey {
  public Script script;
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
    public int[] outputDataLenRange;
    public int[] outputCapacityRange;
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
