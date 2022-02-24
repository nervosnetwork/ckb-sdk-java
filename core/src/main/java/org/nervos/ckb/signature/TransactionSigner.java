package org.nervos.ckb.signature;

import java.util.*;
import org.nervos.ckb.signature.scriptSigner.Secp256k1Blake160ScriptSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;

public class TransactionSigner {
  private Map<Key, ScriptSigner> scriptSignerMap;
  public static TransactionSigner TESTNET_TRANSACTION_SIGNER;
  public static TransactionSigner MAINNET_TRANSACTION_SIGNER;

  static {
    Map<Key, ScriptSigner> testnetScriptSignerMap = new HashMap<>();
    testnetScriptSignerMap.put(
        new Key("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8", "type"),
        new Secp256k1Blake160ScriptSigner());
    // We can register more ScriptSigner for builtin script
    TESTNET_TRANSACTION_SIGNER =
        new TransactionSigner(Collections.unmodifiableMap(testnetScriptSignerMap));
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

  public TransactionSigner register(String codeHash, String hashType, ScriptSigner scriptSigner) {
    scriptSignerMap.put(new Key(codeHash, hashType), scriptSigner);
    return this;
  }

  public void signTx(TransactionWithScriptGroups transaction, Set<Context> contexts) {
    if (contexts == null) {
      throw new RuntimeException("context can't be null");
    }
    Transaction tx = transaction.getTxView();
    List<ScriptGroup> scriptGroups = transaction.getScriptGroups();
    for (ScriptGroup group : scriptGroups) {
      Script script = group.getScript();
      ScriptSigner signer = scriptSignerMap.get(new Key(script.codeHash, script.hashType));
      if (signer == null) {
        throw new RuntimeException("Cannot find ScriptSigner for script " + script);
      }
      boolean isSigned = false;
      for (Context context : contexts) {
        if (signer.signTx(tx, group, context)) {
          isSigned = true;
          break;
        }
      }
      if (isSigned == false) {
        throw new RuntimeException("Cannot find signing secrect for script " + script);
      }
    }
  }

  public void signTx(TransactionWithScriptGroups transaction, String... privateKeys) {
    Contexts contexts = new Contexts();
    contexts.addPrivateKeys(privateKeys);
    signTx(transaction, contexts);
  }

  static class Key {
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
