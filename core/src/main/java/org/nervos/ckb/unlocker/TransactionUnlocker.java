package org.nervos.ckb.unlocker;

import java.util.*;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.unlocker.script.AcpUnlocker;
import org.nervos.ckb.unlocker.script.PwUnlocker;
import org.nervos.ckb.unlocker.script.Secp256K1Blake160Unlocker;
import org.nervos.ckb.utils.Numeric;

public class TransactionUnlocker {
  private Map<Key, ScriptUnlocker> scriptUnlockerMap;
  public static TransactionUnlocker TESTNET_TRANSACTION_UNLOCKER;
  public static TransactionUnlocker MAINNET_TRANSACTION_UNLOCKER;

  static {
    TESTNET_TRANSACTION_UNLOCKER = new TransactionUnlocker();
    // We can register more ScriptSigner for builtin script
    TESTNET_TRANSACTION_UNLOCKER.registerLockScriptUnlocker(
        "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
        Secp256K1Blake160Unlocker.getInstance());
    TESTNET_TRANSACTION_UNLOCKER.registerLockScriptUnlocker(
        "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
        AcpUnlocker.getInstance());
    TESTNET_TRANSACTION_UNLOCKER.registerLockScriptUnlocker(
        "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63",
        PwUnlocker.getInstance());
    TESTNET_TRANSACTION_UNLOCKER.scriptUnlockerMap =
        Collections.unmodifiableMap(TESTNET_TRANSACTION_UNLOCKER.scriptUnlockerMap);
  }

  public TransactionUnlocker() {
    this(new HashMap<>());
  }

  public TransactionUnlocker(Map<Key, ScriptUnlocker> scriptSignerMap) {
    this.scriptUnlockerMap = scriptSignerMap;
  }

  public TransactionUnlocker(TransactionUnlocker s) {
    scriptUnlockerMap = new HashMap<>();
    for (Map.Entry<Key, ScriptUnlocker> entry : s.scriptUnlockerMap.entrySet()) {
      scriptUnlockerMap.put(entry.getKey(), entry.getValue());
    }
  }

  private TransactionUnlocker register(
      String codeHash, String hashType, ScriptType scriptType, ScriptUnlocker scriptUnlocker) {
    scriptUnlockerMap.put(new Key(codeHash, hashType, scriptType), scriptUnlocker);
    return this;
  }

  public TransactionUnlocker registerTypeScriptUnlocker(
      String codeHash, ScriptUnlocker scriptUnlocker) {
    return register(codeHash, "type", ScriptType.TYPE, scriptUnlocker);
  }

  public TransactionUnlocker registerLockScriptUnlocker(
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
      if (isValidScriptGroup(group) == false) {
        continue;
      }

      Script script = group.getScript();
      ScriptUnlocker unlocker =
          scriptUnlockerMap.get(new Key(script.codeHash, script.hashType, group.getScriptType()));
      if (unlocker != null) {
        for (Context context : contexts) {
          if (unlocker.unlockScript(tx, group, context)) {
            signedGroupsIndices.add(i);
            break;
          }
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

  private boolean isValidScriptGroup(ScriptGroup scriptGroup) {
    if (scriptGroup == null
        || scriptGroup.getScript() == null
        || scriptGroup.getScriptType() == null) {
      return false;
    }

    boolean isEmptyInputIndices =
        (scriptGroup.getInputIndices() == null || scriptGroup.getInputIndices().isEmpty());
    boolean isEmptyOutputIndices =
        (scriptGroup.getOutputIndices() == null || scriptGroup.getOutputIndices().isEmpty());

    ScriptType scriptType = scriptGroup.getScriptType();
    if (scriptType == ScriptType.LOCK) {
      return !isEmptyInputIndices;
    } else if (scriptType == ScriptType.TYPE) {
      return (!isEmptyInputIndices || isEmptyOutputIndices);
    }

    return false;
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
