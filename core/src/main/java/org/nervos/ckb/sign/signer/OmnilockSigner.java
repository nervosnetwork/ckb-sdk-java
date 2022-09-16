package org.nervos.ckb.sign.signer;

import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.sign.omnilock.OmnilockConfig;
import org.nervos.ckb.sign.omnilock.OmnilockWitnessLock;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.type.WitnessArgs;

import java.util.Arrays;
import java.util.List;

public class OmnilockSigner implements ScriptSigner {

  @Override
  public boolean signTransaction(Transaction transaction, ScriptGroup scriptGroup, Context context) {
    OmnilockConfig omnilockConfig;
    if (!(context.getPayload() instanceof OmnilockConfig)) {
      return false;
    }
    omnilockConfig = (OmnilockConfig) context.getPayload();
    byte[] args = scriptGroup.getScript().args;
    // Check if script args is matched with given omnilockConfig
    if (!Arrays.equals(args, omnilockConfig.encode())) {
      return false;
    }

    List<byte[]> witnesses = transaction.witnesses;
    int index = scriptGroup.getInputIndices().get(0);
    WitnessArgs witnessArgs = WitnessArgs.unpack(witnesses.get(index));
    OmnilockWitnessLock omnilockWitnessLock;
    switch (omnilockConfig.getMode()) {
      case AUTH:
        omnilockWitnessLock = signForAuthMode(transaction, scriptGroup, context.getKeyPair(), omnilockConfig);
        break;
      case ADMINISTRATOR:
        omnilockWitnessLock = signForAdministratorMode(transaction, scriptGroup, context.getKeyPair(), omnilockConfig);
        break;
      default:
        throw new IllegalArgumentException("Unknown Omnilock mode " + omnilockConfig.getMode());
    }
    if (omnilockWitnessLock != null) {
      witnessArgs.setLock(omnilockWitnessLock.pack().toByteArray());
      witnesses.set(index, witnessArgs.pack().toByteArray());
      return true;
    } else {
      return false;
    }
  }

  private static OmnilockWitnessLock signForAuthMode(Transaction transaction, ScriptGroup scriptGroup, ECKeyPair keyPair, OmnilockConfig omnilockConfig) {
    byte[] authArgs = Arrays.copyOfRange(scriptGroup.getScript().args, 1, 21);
    byte[] signature;
    // TODO: complete
    switch (omnilockConfig.getAuthenticationArgs().getFlag()) {
      case CKB_SECP256K1_BLAKE160:
        byte[] hash = Blake2b.digest(keyPair.getEncodedPublicKey(true));
        hash = Arrays.copyOfRange(hash, 0, 20);
        if (!Arrays.equals(authArgs, hash)) {
          return null;
        }
        signature = Secp256k1Blake160SighashAllSigner.signTransaction(transaction, scriptGroup, keyPair);
        break;
      case ETHEREUM:
        throw new UnsupportedOperationException("Ethereum");
      case EOS:
        throw new UnsupportedOperationException("EOS");
      case TRON:
        throw new UnsupportedOperationException("TRON");
      case BITCOIN:
        throw new UnsupportedOperationException("Bitcoin");
      case DOGECOIN:
        throw new UnsupportedOperationException("Dogecoin");
      case CKB_MULTI_SIG:
        Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript = omnilockConfig.getMultisigScript();
        if (multisigScript == null) {
          return null;
        }
        if (!Arrays.equals(authArgs, multisigScript.computeHash())) {
          return null;
        }
        signature = Secp256k1Blake160MultisigAllSigner.signTransaction(transaction, scriptGroup, keyPair, multisigScript);
        // TODO: read current witness in transaction and set signature back
        break;
      case LOCK_SCRIPT_HASH:
        signature = new byte[0];
        break;
      case EXEC:
        throw new UnsupportedOperationException("Exec");
      case DYNAMIC_LINKING:
        throw new UnsupportedOperationException("Dynamic linking");
      default:
        throw new IllegalArgumentException("Unknown auth flag " + omnilockConfig.getOmnilockArgs().getFlag());
    }
    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    omnilockWitnessLock.setSignature(signature);
    return omnilockWitnessLock;
  }

  private static OmnilockWitnessLock signForAdministratorMode(Transaction transaction, ScriptGroup scriptGroup, ECKeyPair keyPair, OmnilockConfig omnilockConfig) {
    byte[] signature = null;
    switch (omnilockConfig.getOmnilockIdentity().getIdentity().getFlag()) {
      case CKB_SECP256K1_BLAKE160:
        throw new UnsupportedOperationException("CKB_SECP256K1_BLAKE160");
      case LOCK_SCRIPT_HASH:
        break;
    }
    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    omnilockWitnessLock.setOmnilockIdentity(omnilockConfig.getOmnilockIdentity());
    omnilockWitnessLock.setSignature(signature);
    return omnilockWitnessLock;
  }
}
