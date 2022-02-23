package org.nervos.ckb.signature;

import java.util.*;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;

public class ScriptSignerManager {
  private Map<Key, ScriptSigner> scriptSignerMap;

  static {

  }

  public ScriptSignerManager() {
    this(new HashMap<>());
  }

  public ScriptSignerManager(Map<Key, ScriptSigner> scriptSignerMap) {
    this.scriptSignerMap = scriptSignerMap;
  }

  // TODO: implement Script' equals() and hashCode()
  // TODO: clone script to void object being modified
  public ScriptSignerManager register(String codeHash, String hashType , ScriptSigner scriptSigner) {
    scriptSignerMap.put(new Key(codeHash, hashType), scriptSigner);
    return this;
  }

  public void signTx(TransactionWithScriptGroups transactionTemplate, Set context) {
    if (context == null) {
      throw new RuntimeException("context can't be null");
    }
    Transaction tx = transactionTemplate.getTxView();
    List<ScriptGroup> scriptGroups = transactionTemplate.getScriptGroups();
    for (ScriptGroup group : scriptGroups) {
      Script script = group.getScript();
      ScriptSigner signer = scriptSignerMap.get(new Key(script.codeHash, script.hashType));
      if (signer == null) {
        throw new RuntimeException("Cannot find ScriptSigner for script " + script);
      }
      boolean isSigned = false;
      for (Object c: context) {
        if (signer.canSign(script.args, c)) {
          signer.signTx(tx, group, c);
          isSigned = true;
          break;
        }
      }
      if (isSigned == false) {
        throw new RuntimeException("Cannot find signing secrect for script " + script);
      }
    }
  }

  class Key {
    private String codeHash;
    private String hashType;

    public Key(String codeHash, String hashType) {
      this.codeHash = codeHash;
      this.hashType = hashType;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Key key = (Key) o;
      return codeHash.equals(key.codeHash) && hashType.equals(key.hashType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(codeHash, hashType);
    }
  }
}
