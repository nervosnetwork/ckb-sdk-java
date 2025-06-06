package org.nervos.ckb.sign;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.signer.*;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.Numeric;

import java.util.*;

public class TransactionSigner {
  private Map<Key, ScriptSigner> scriptSignerMap;
  private static TransactionSigner TESTNET_TRANSACTION_SIGNER;
  private static TransactionSigner MAINNET_TRANSACTION_SIGNER;

  static {
    TESTNET_TRANSACTION_SIGNER = new TransactionSigner()
        .registerLockScriptSigner(
            Script.SECP256K1_BLAKE160_SIGNHASH_ALL_CODE_HASH, new Secp256k1Blake160SighashAllSigner())
        .registerLockScriptSigner(
            Script.SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_LEGACY, new Secp256k1Blake160MultisigAllSigner())
        .registerLockScriptData1Signer(
            Script.SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_V2, new Secp256k1Blake160MultisigAllSigner())
        .registerLockScriptSigner(
            Script.ANY_CAN_PAY_CODE_HASH_TESTNET, new AcpSigner())
        .registerLockScriptSigner(
            Script.PW_LOCK_CODE_HASH_TESTNET, new PwSigner())
        .registerLockScriptSigner(
            Script.OMNILOCK_CODE_HASH_TESTNET, new OmnilockSigner());

    MAINNET_TRANSACTION_SIGNER = new TransactionSigner()
        .registerLockScriptSigner(
            Script.SECP256K1_BLAKE160_SIGNHASH_ALL_CODE_HASH, new Secp256k1Blake160SighashAllSigner())
        .registerLockScriptSigner(
            Script.SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_LEGACY, new Secp256k1Blake160MultisigAllSigner())
        .registerLockScriptData1Signer(
            Script.SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_V2, new Secp256k1Blake160MultisigAllSigner())
        .registerLockScriptSigner(
            Script.ANY_CAN_PAY_CODE_HASH_MAINNET, new AcpSigner())
        .registerLockScriptSigner(
            Script.PW_LOCK_CODE_HASH_MAINNET, new PwSigner())
        .registerLockScriptSigner(
            Script.OMNILOCK_CODE_HASH_MAINNET, new OmnilockSigner());
  }

  public TransactionSigner() {
    this(new HashMap<>());
  }

  public TransactionSigner(Map<Key, ScriptSigner> scriptSignerMap) {
    this.scriptSignerMap = scriptSignerMap;
  }

  public TransactionSigner(TransactionSigner s) {
    scriptSignerMap = new HashMap<>();
    for (Map.Entry<Key, ScriptSigner> entry: s.scriptSignerMap.entrySet()) {
      scriptSignerMap.put(entry.getKey(), entry.getValue());
    }
  }

  public static TransactionSigner getInstance(Network network) {
    if (network == Network.TESTNET) {
      return TESTNET_TRANSACTION_SIGNER;
    } else if (network == Network.MAINNET) {
      return MAINNET_TRANSACTION_SIGNER;
    } else {
      throw new IllegalArgumentException("Unsupported network: " + network);
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

  public TransactionSigner registerLockScriptData1Signer(byte[] codeHash, ScriptSigner scriptSigner) {
    return register(codeHash, Script.HashType.DATA1, ScriptType.LOCK, scriptSigner);
  }

  public TransactionSigner registerLockScriptSigner(String codeHash, ScriptSigner scriptSigner) {
    return registerLockScriptSigner(Numeric.hexStringToByteArray(codeHash), scriptSigner);
  }

  /**
   * signTransaction signs the transaction using registered ScriptSigners.
   *
   * @param transaction Transaction to be signed.
   * @param contexts Contexts for {@link org.nervos.ckb.sign.ScriptSigner}.
   * @return signed groups indices.
   */
  public Set<Integer> signTransaction(
      TransactionWithScriptGroups transaction, Context... contexts) {
    return signTransaction(transaction, new HashSet<>(Arrays.asList(contexts)));
  }

  /**
   * signTransaction signs the transaction using registered ScriptSigners.
   *
   * @param transaction Transaction to be signed.
   * @param contexts Contexts for {@link org.nervos.ckb.sign.ScriptSigner}.
   * @return signed groups indices.
   */
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
        throw new IllegalArgumentException("invalid script group at index " + i);
      }

      Script script = group.getScript();
      ScriptSigner signer =
          scriptSignerMap.get(new Key(script.codeHash, script.hashType, group.getGroupType()));
      if (signer != null) {
        for (Context context: contexts) {
          if (signer.signTransaction(tx, group, context)) {
            signedGroupsIndices.add(i);
            break;
          }
        }
      }
    }
    return signedGroupsIndices;
  }

  /**
   * signTransaction signs the transaction using registered ScriptSigners.
   *
   * @param transaction Transaction to be signed.
   * @param privateKeys Each private key is wrapped in one {@link Context}. These contexts will be passed to ScriptSigners.
   * @return signed groups indices.
   */
  public Set<Integer> signTransaction(
      TransactionWithScriptGroups transaction, String... privateKeys) {
    Contexts contexts = new Contexts();
    contexts.addPrivateKeys(privateKeys);
    return signTransaction(transaction, contexts);
  }

  private boolean isValidScriptGroup(ScriptGroup scriptGroup) {
    if (scriptGroup == null
        || scriptGroup.getScript() == null
        || scriptGroup.getGroupType() == null) {
      return false;
    }

    boolean isEmptyInputIndices =
        (scriptGroup.getInputIndices() == null || scriptGroup.getInputIndices().isEmpty());
    boolean isEmptyOutputIndices =
        (scriptGroup.getOutputIndices() == null || scriptGroup.getOutputIndices().isEmpty());

    ScriptType scriptType = scriptGroup.getGroupType();
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
      if (!Arrays.equals(codeHash, key.codeHash)) return false;
      if (hashType != key.hashType) return false;
      return scriptType == key.scriptType;
    }

    @Override
    public int hashCode() {
      int result = Arrays.hashCode(codeHash);
      result = 31 * result + hashType.hashCode();
      result = 31 * result + scriptType.hashCode();
      return result;
    }
  }
}
