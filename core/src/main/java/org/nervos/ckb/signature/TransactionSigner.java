package org.nervos.ckb.signature;

import java.util.*;
import org.nervos.ckb.signature.scriptSigner.Secp256k1Blake160ScriptSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;

public class TransactionSigner {
  private Map<Key, ScriptSigner> scriptSignerMap;
  public static TransactionSigner TESTNET_TRANSACTION_SIGNER;
  public static TransactionSigner MAINNET_TRANSACTION_SIGNER;

  static {
    TESTNET_TRANSACTION_SIGNER = new TransactionSigner();
    // We can register more ScriptSigner for builtin script
    TESTNET_TRANSACTION_SIGNER.registerLockScriptSigner(
        "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
        new Secp256k1Blake160ScriptSigner());
    TESTNET_TRANSACTION_SIGNER.scriptSignerMap =
        Collections.unmodifiableMap(TESTNET_TRANSACTION_SIGNER.scriptSignerMap);
  }

  public TransactionSigner() {
    this(new HashMap<>());
  }

  public TransactionSigner(Map<Key, ScriptSigner> scriptSignerMap) {
    this.scriptSignerMap = scriptSignerMap;
  }

  public TransactionSigner(TransactionSigner s) {
    scriptSignerMap = new HashMap<>();
    for (Map.Entry<Key, ScriptSigner> entry : s.scriptSignerMap.entrySet()) {
      scriptSignerMap.put(entry.getKey(), entry.getValue());
    }
  }

  private TransactionSigner register(
      String codeHash, String hashType, ScriptType scriptType, ScriptSigner scriptSigner) {
    scriptSignerMap.put(new Key(codeHash, hashType, scriptType), scriptSigner);
    return this;
  }

  public TransactionSigner registerTypeScriptSigner(String codeHash, ScriptSigner scriptSigner) {
    return register(codeHash, "type", ScriptType.TYPE, scriptSigner);
  }

  public TransactionSigner registerLockScriptSigner(String codeHash, ScriptSigner scriptSigner) {
    return register(codeHash, "type", ScriptType.LOCK, scriptSigner);
  }

  public Set<Integer> signTx(TransactionWithScriptGroups transaction, Set<Context> contexts) {
    Set<Integer> signedGroupsIndices = new HashSet<>();
    if (contexts == null) {
      return signedGroupsIndices;
    }
    Transaction tx = transaction.getTxView();
    List<ScriptGroup> scriptGroups = transaction.getScriptGroups();
    for (int i = 0; i < scriptGroups.size(); i++) {
      ScriptGroup group = scriptGroups.get(i);
      Script script = group.getScript();
      ScriptSigner signer =
          scriptSignerMap.get(new Key(script.codeHash, script.hashType, group.getScriptType()));
      if (signer == null) {
        continue;
      }
      for (Context context : contexts) {
        if (signer.signTx(tx, group, context)) {
          signedGroupsIndices.add(i);
          break;
        }
      }
    }
    return signedGroupsIndices;
  }

  public void signTx(TransactionWithScriptGroups transaction, String... privateKeys) {
    Contexts contexts = new Contexts();
    contexts.addPrivateKeys(privateKeys);
    signTx(transaction, contexts);
  }

  static class Key {
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
