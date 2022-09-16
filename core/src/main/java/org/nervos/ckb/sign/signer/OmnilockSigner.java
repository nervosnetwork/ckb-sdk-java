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

  // TODO: return void
  private static OmnilockWitnessLock signForAuthMode(Transaction transaction, ScriptGroup scriptGroup, ECKeyPair keyPair, OmnilockConfig omnilockConfig) {
    byte[] authArgs = Arrays.copyOfRange(scriptGroup.getScript().args, 1, 21);
    // TODO: complete
    int firstIndex = scriptGroup.getInputIndices().get(0);
    byte[] firstWitness = transaction.witnesses.get(firstIndex);


    // witness placeholder should be set before signing
    WitnessArgs witnessArgs = WitnessArgs.unpack(firstWitness);
    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    switch (omnilockConfig.getAuthenticationArgs().getFlag()) {
      case CKB_SECP256K1_BLAKE160:
        byte[] hash = Blake2b.digest(keyPair.getEncodedPublicKey(true));
        hash = Arrays.copyOfRange(hash, 0, 20);
        if (!Arrays.equals(authArgs, hash)) {
          return null;
        }
        // Prepare witness placeholder
        omnilockWitnessLock.setSignature(new byte[65]);
        witnessArgs.setLock(new byte[omnilockWitnessLock.pack().toByteArray().length]);
        byte[] witnessPlaceholder = witnessArgs.pack().toByteArray();
        // Sign
        byte[] signature = Secp256k1Blake160SighashAllSigner.signTransaction(transaction, scriptGroup, witnessPlaceholder, keyPair);
        omnilockWitnessLock.setSignature(signature);
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
        if (multisigScript == null || !Arrays.equals(authArgs, multisigScript.computeHash())) {
          return null;
        }
        boolean inMultisigScript = false;
        for (byte[] v: multisigScript.getKeysHashes()) {
          hash = Blake2b.digest(keyPair.getEncodedPublicKey(true));
          hash = Arrays.copyOfRange(hash, 0, 20);
          if (Arrays.equals(v, hash)) {
            inMultisigScript = true;
            break;
          }
        }
        if (!inMultisigScript) {
          return null;
        }

        // Prepare witness placeholder
        WitnessArgs witnessArgsPlaceholder = WitnessArgs.unpack(firstWitness);
        omnilockWitnessLock.setSignature(multisigScript.witnessPlaceholderInLock());
        witnessArgsPlaceholder.setLock(new byte[omnilockWitnessLock.pack().toByteArray().length]);
        witnessPlaceholder = witnessArgsPlaceholder.pack().toByteArray();
        // Sign
        signature = Secp256k1Blake160SighashAllSigner.signTransaction(transaction, scriptGroup, witnessPlaceholder, keyPair);

        // get existent signature
        byte[] oldSignature;
        byte[] lockBytes = witnessArgs.getLock();
        if (isEmpty(lockBytes, 0, lockBytes.length)) {
          oldSignature = multisigScript.witnessPlaceholderInLock();
        } else {
          oldSignature = OmnilockWitnessLock.unpack(lockBytes).getSignature();
        }

        // set segment signature to signature
        signature = setSignatureToWitness(oldSignature, signature, multisigScript);
        omnilockWitnessLock.setSignature(signature);
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
    return omnilockWitnessLock;
  }

  private static byte[] setSignatureToWitness(byte[] signatures, byte[] signature, Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript) {
    int pos = multisigScript.encode().length;
    for (int i = 0; i < multisigScript.getThreshold(); i++) {
      if (isEmpty(signatures, pos, 65)) {
        System.arraycopy(signature, 0, signatures, pos, 65);
        break;
      }
      pos += 65;
    }
    return signatures;
  }

  private static boolean isEmpty(byte[] lock, int start, int length) {
    for (int i = start; i < start + length; i++) {
      if (lock[i] != 0) {
        return false;
      }
    }
    return true;
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
