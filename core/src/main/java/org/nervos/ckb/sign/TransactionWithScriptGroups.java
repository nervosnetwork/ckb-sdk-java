package org.nervos.ckb.sign;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;

public class TransactionWithScriptGroups {
  @SerializedName("tx_view")
  private Transaction txView;

  @SerializedName("script_groups")
  private List<ScriptGroup> scriptGroups;

  public Transaction getTxView() {
    return txView;
  }

  public void setTxView(Transaction txView) {
    this.txView = txView;
  }

  public List<ScriptGroup> getScriptGroups() {
    return scriptGroups;
  }

  public void setScriptGroups(List<ScriptGroup> scriptGroups) {
    this.scriptGroups = scriptGroups;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private Transaction txView;
    private List<ScriptGroup> scriptGroups;

    private Builder() {
      this.scriptGroups = new ArrayList<>();
    }

    public Builder setTxView(Transaction txView) {
      this.txView = txView;
      return this;
    }

    public Builder setScriptGroups(List<ScriptGroup> scriptGroups) {
      this.scriptGroups = scriptGroups;
      return this;
    }

    public Builder addScriptGroup(ScriptGroup scriptGroup) {
      this.scriptGroups.add(scriptGroup);
      return this;
    }

    public Builder addLockScriptGroup(Script script, int... inputIndices) {
      ScriptGroup scriptGroup =
          ScriptGroup.builder()
              .setScriptType(ScriptType.LOCK)
              .setScript(script)
              .addInputIndices(inputIndices)
              .build();
      return addScriptGroup(scriptGroup);
    }

    public Builder addLockScriptGroup(byte[] codeHash, byte[] args, int... inputIndices) {
      Script script = new Script(codeHash, args, Script.HashType.TYPE);
      return addLockScriptGroup(script, inputIndices);
    }

    public TransactionWithScriptGroups build() {
      TransactionWithScriptGroups transactionWithScriptGroups = new TransactionWithScriptGroups();
      transactionWithScriptGroups.setTxView(txView);
      transactionWithScriptGroups.setScriptGroups(scriptGroups);
      return transactionWithScriptGroups;
    }
  }
}
