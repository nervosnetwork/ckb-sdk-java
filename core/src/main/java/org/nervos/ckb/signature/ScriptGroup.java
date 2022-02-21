package org.nervos.ckb.signature;

import java.util.List;
import org.nervos.ckb.type.Script;

public class ScriptGroup {
  private Script script;
  private ScriptType scriptType;
  private List<Integer> inputIndices;
  private List<Integer> outputIndices;

  public Script getScript() {
    return script;
  }

  public void setScript(Script script) {
    this.script = script;
  }

  public ScriptType getScriptType() {
    return scriptType;
  }

  public void setScriptType(ScriptType scriptType) {
    this.scriptType = scriptType;
  }

  public List<Integer> getInputIndices() {
    return inputIndices;
  }

  public void setInputIndices(List<Integer> inputIndices) {
    this.inputIndices = inputIndices;
  }

  public List<Integer> getOutputIndices() {
    return outputIndices;
  }

  public void setOutputIndices(List<Integer> outputIndices) {
    this.outputIndices = outputIndices;
  }
}
