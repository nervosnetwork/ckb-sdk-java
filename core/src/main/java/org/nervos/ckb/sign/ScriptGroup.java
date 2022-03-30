package org.nervos.ckb.sign;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

public class ScriptGroup {
  private Script script;

  @SerializedName("script_type")
  private ScriptType scriptType;

  @SerializedName("input_indices")
  private List<Integer> inputIndices;

  @SerializedName("output_indices")
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

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Script script;
    private ScriptType scriptType;
    private List<Integer> inputIndices;
    private List<Integer> outputIndices;

    private Builder() {
      this.inputIndices = new ArrayList<>();
      this.outputIndices = new ArrayList<>();
    }

    public static Builder aScriptGroup() {
      return new Builder();
    }

    public Builder setScript(Script script) {
      this.script = script;
      return this;
    }

    public Builder setScript(byte[] codeHash, byte[] args, Script.HashType type) {
      Script script = new Script(codeHash, args, type);
      return setScript(script);
    }

    public Builder setScript(byte[] codeHash, byte[] args) {
      Script script = new Script(codeHash, args, Script.HashType.TYPE);
      return setScript(script);
    }

    public Builder setScriptType(ScriptType scriptType) {
      this.scriptType = scriptType;
      return this;
    }

    public Builder setInputIndices(List<Integer> inputIndices) {
      this.inputIndices = inputIndices;
      return this;
    }

    public Builder addInputIndices(int... indices) {
      for (int index : indices) {
        this.inputIndices.add(index);
      }
      return this;
    }

    public Builder setOutputIndices(List<Integer> outputIndices) {
      this.outputIndices = outputIndices;
      return this;
    }

    public Builder addOutputIndices(int... indices) {
      for (int index : indices) {
        this.outputIndices.add(index);
      }
      return this;
    }

    public ScriptGroup build() {
      ScriptGroup scriptGroup = new ScriptGroup();
      scriptGroup.setScript(script);
      scriptGroup.setScriptType(scriptType);
      scriptGroup.setInputIndices(inputIndices);
      scriptGroup.setOutputIndices(outputIndices);
      return scriptGroup;
    }
  }
}
