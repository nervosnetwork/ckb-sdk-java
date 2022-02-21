package org.nervos.ckb.signature;

import java.util.List;
import org.nervos.ckb.type.transaction.Transaction;

public class TransactionWithScriptGroups {
  private Transaction txView;
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
}
