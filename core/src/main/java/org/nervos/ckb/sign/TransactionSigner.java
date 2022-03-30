package org.nervos.ckb.sign;

import java.util.*;
import org.nervos.ckb.sign.signer.AcpSigner;
import org.nervos.ckb.sign.signer.PwSigner;
import org.nervos.ckb.sign.signer.Secp256k1Blake160SighashAllSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
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
        Secp256k1Blake160SighashAllSigner.getInstance());
    TESTNET_TRANSACTION_SIGNER.registerLockScriptSigner(
        "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
        AcpSigner.getInstance());
    TESTNET_TRANSACTION_SIGNER.registerLockScriptSigner(
        "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63",
        PwSigner.getInstance());
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
          byte[] codeHash, Script.HashType hashType, ScriptType scriptType, ScriptSigner scriptSigner) {
    scriptSignerMap.put(new Key(codeHash, hashType, scriptType), scriptSigner);
    return this;
  }

  public TransactionSigner registerTypeScriptSigner(byte[] codeHash, ScriptSigner scriptSigner) {
    return register(codeHash, Script.HashType.TYPE, ScriptType.TYPE, scriptSigner);
  }

  public TransactionSigner registerTypeScriptSigner(String codeHash, ScriptSigner scriptSigner) {
    return registerTypeScriptSigner(Numeric.hexStringToByteArray(codeHash), scriptSigner);
  }

  public TransactionSigner registerLockScriptSigner(byte[] codeHash, ScriptSigner scriptSigner) {
    return register(codeHash, Script.HashType.TYPE, ScriptType.LOCK, scriptSigner);
  }

  public TransactionSigner registerLockScriptSigner(String codeHash, ScriptSigner scriptSigner) {
    return registerLockScriptSigner(Numeric.hexStringToByteArray(codeHash), scriptSigner);
  }

  public Set<Integer> signTransaction(
      TransactionWithScriptGroups transaction, Set<Context> contexts) {
    Set<Integer> signedGroupsIndices = new HashSet<>();
    if (contexts == null) {
      return signedGroupsIndices;
    }
    Transaction tx = transaction.getTxView();
    List<ScriptGroup> scriptGroups = transaction.getScriptGroups();
    for (int i = 0; i < scriptGroups.size(); i++) {
      ScriptGroup group = scriptGroups.get(i);
      if (!isValidScriptGroup(group)) {
        continue;
      }

      Script script = group.getScript();
      ScriptSigner signer =
          scriptSignerMap.get(new Key(script.codeHash, script.hashType, group.getScriptType()));
      if (signer != null) {
        for (Context context : contexts) {
          if (signer.signTransaction(tx, group, context)) {
            signedGroupsIndices.add(i);
            break;
          }
        }
      }
    }
    return signedGroupsIndices;
  }

  public Set<Integer> signTransaction(
      TransactionWithScriptGroups transaction, String... privateKeys) {
    Contexts contexts = new Contexts();
    contexts.addPrivateKeys(privateKeys);
    return signTransaction(transaction, contexts);
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
      return (!isEmptyInputIndices || !isEmptyOutputIndices);
    }

    return false;
  }

  private static class Key {
    private byte[] codeHash;
    private Script.HashType hashType;
    private ScriptType scriptType;

    public Key(byte[] codeHash, Script.HashType hashType, ScriptType scriptType) {
      this.codeHash = codeHash;
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
