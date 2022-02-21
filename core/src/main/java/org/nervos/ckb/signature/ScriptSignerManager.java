package org.nervos.ckb.signature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;

public class ScriptSignerManager {
  private Map<Script, ScriptSigner> scriptSignerMap;

  public ScriptSignerManager() {
    this.scriptSignerMap = new HashMap<>();
  }

  // TODO: implement Script' equals() and hashCode()
  // TODO: clone script to void object being modified
  // Do we need to register with `ScriptType` as parameter?
  public ScriptSignerManager register(Script script, ScriptSigner scriptSigner) {
    scriptSignerMap.put(script, scriptSigner);
    return this;
  }

  public void signTx(TransactionWithScriptGroups transactionTemplate) {
    Transaction tx = transactionTemplate.getTxView();
    List<ScriptGroup> scriptGroups = transactionTemplate.getScriptGroups();
    for (ScriptGroup group : scriptGroups) {
      Script script = group.getScript();
      ScriptSigner signer = scriptSignerMap.get(script);
      if (signer != null) {
        signer.signTx(tx, group);
      } else {
        throw new RuntimeException("Cannot find ScriptSigner for script " + script);
      }
    }
  }
}
