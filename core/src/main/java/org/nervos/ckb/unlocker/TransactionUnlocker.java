package org.nervos.ckb.unlocker;

import java.util.*;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.unlocker.script.Secp256K1Blake160ScriptUnlocker;
import org.nervos.ckb.utils.Numeric;

public class TransactionUnlocker {
  private Map<Key, ScriptUnlocker> scriptSignerMap;
  public static TransactionUnlocker TESTNET_TRANSACTION_UNLOCKER;
  public static TransactionUnlocker MAINNET_TRANSACTION_UNLOCKER;

  static {
    TESTNET_TRANSACTION_UNLOCKER = new TransactionUnlocker();
    // We can register more ScriptSigner for builtin script
    TESTNET_TRANSACTION_UNLOCKER.registerLockScriptSigner(
        "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
        new Secp256K1Blake160ScriptUnlocker());
    TESTNET_TRANSACTION_UNLOCKER.scriptSignerMap =
        Collections.unmodifiableMap(TESTNET_TRANSACTION_UNLOCKER.scriptSignerMap);
  }

  public TransactionUnlocker() {
    this(new HashMap<>());
  }

  public TransactionUnlocker(Map<Key, ScriptUnlocker> scriptSignerMap) {
    this.scriptSignerMap = scriptSignerMap;
  }

  public TransactionUnlocker(TransactionUnlocker s) {
    scriptSignerMap = new HashMap<>();
    for (Map.Entry<Key, ScriptUnlocker> entry : s.scriptSignerMap.entrySet()) {
      scriptSignerMap.put(entry.getKey(), entry.getValue());
    }
  }

  private TransactionUnlocker register(
      String codeHash, String hashType, ScriptType scriptType, ScriptUnlocker scriptUnlocker) {
    scriptSignerMap.put(new Key(codeHash, hashType, scriptType), scriptUnlocker);
    return this;
  }

  public TransactionUnlocker registerTypeScriptSigner(
      String codeHash, ScriptUnlocker scriptUnlocker) {
    return register(codeHash, "type", ScriptType.TYPE, scriptUnlocker);
  }

  public TransactionUnlocker registerLockScriptSigner(
      String codeHash, ScriptUnlocker scriptUnlocker) {
    return register(codeHash, "type", ScriptType.LOCK, scriptUnlocker);
  }

  public Set<Integer> unlockTransaction(
      TransactionWithScriptGroups transaction, Set<Context> contexts) {
    Set<Integer> signedGroupsIndices = new HashSet<>();
    if (contexts == null) {
      return signedGroupsIndices;
    }
    Transaction tx = transaction.getTxView();
    List<ScriptGroup> scriptGroups = transaction.getScriptGroups();
    for (int i = 0; i < scriptGroups.size(); i++) {
      ScriptGroup group = scriptGroups.get(i);
      Script script = group.getScript();
      ScriptUnlocker signer =
          scriptSignerMap.get(new Key(script.codeHash, script.hashType, group.getScriptType()));
      if (signer == null) {
        continue;
      }
      for (Context context : contexts) {
        if (signer.unlockScript(tx, group, context)) {
          signedGroupsIndices.add(i);
          break;
        }
      }
    }
    return signedGroupsIndices;
  }

  public Set<Integer> unlockTransaction(
      TransactionWithScriptGroups transaction, String... privateKeys) {
    Contexts contexts = new Contexts();
    contexts.addPrivateKeys(privateKeys);
    return unlockTransaction(transaction, contexts);
  }

  private static class Key {
    private String codeHash;
    private String hashType;
    private ScriptType scriptType;

    public Key(String codeHash, String hashType, ScriptType scriptType) {
      this.codeHash = Numeric.cleanHexPrefix(codeHash);
      this.hashType = hashType;
      this.scriptType = scriptType;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Key key = (Key) o;
      return codeHash.equals(key.codeHash)
          && hashType.equals(key.hashType)
          && scriptType == key.scriptType;
    }

    @Override
    public int hashCode() {
      return Objects.hash(codeHash, hashType, scriptType);
    }
  }
}
