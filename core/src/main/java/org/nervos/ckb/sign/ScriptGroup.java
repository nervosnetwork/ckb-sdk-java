package org.nervos.ckb.sign;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

import java.util.ArrayList;
import java.util.List;

public class ScriptGroup {
  private Script script;
  private ScriptType groupType;
  private List<Integer> inputIndices = new ArrayList<>();
  private List<Integer> outputIndices = new ArrayList<>();

  public ScriptGroup(Script script, ScriptType groupType) {
    this.script = script;
    this.groupType = groupType;
  }

  public Script getScript() {
    return script;
  }

  public void setScript(Script script) {
    this.script = script;
  }

  public ScriptType getGroupType() {
    return groupType;
  }

  public void setGroupType(ScriptType groupType) {
    this.groupType = groupType;
  }

  public static ScriptGroup new_type(Script script) {
    return new ScriptGroup(script, ScriptType.TYPE);
  }

  public static ScriptGroup new_lock(Script script) {
    return new ScriptGroup(script, ScriptType.LOCK);
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
    private ScriptType groupType;
    private List<Integer> inputIndices;
    private List<Integer> outputIndices;

    private Builder() {
      this.inputIndices = new ArrayList<>();
      this.outputIndices = new ArrayList<>();
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

    public Builder setGroupType(ScriptType groupType) {
      this.groupType = groupType;
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
      ScriptGroup scriptGroup = new ScriptGroup(script, groupType);
      scriptGroup.setInputIndices(inputIndices);
      scriptGroup.setOutputIndices(outputIndices);
      return scriptGroup;
    }
  }
}
