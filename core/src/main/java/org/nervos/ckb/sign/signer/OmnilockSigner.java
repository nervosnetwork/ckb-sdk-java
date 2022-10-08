package org.nervos.ckb.sign.signer;

import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.sign.omnilock.OmnilockArgs;
import org.nervos.ckb.sign.omnilock.OmnilockIdentity;
import org.nervos.ckb.sign.omnilock.OmnilockWitnessLock;
import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.type.WitnessArgs;

import java.util.Arrays;
import java.util.List;

public class OmnilockSigner implements ScriptSigner {

  @Override
  public boolean signTransaction(Transaction transaction, ScriptGroup scriptGroup, Context context) {
    if (!(context.getPayload() instanceof Configuration)) {
      return false;
    }
    Configuration config = (Configuration) context.getPayload();
    byte[] args = scriptGroup.getScript().args;
    // Check if script args is matched with given omnilockConfig
    if (!Arrays.equals(args, config.getOmnilockArgs().encode())) {
      return false;
    }

    List<byte[]> witnesses = transaction.witnesses;
    int index = scriptGroup.getInputIndices().get(0);
    WitnessArgs witnessArgs = WitnessArgs.unpack(witnesses.get(index));
    OmnilockWitnessLock omnilockWitnessLock;
    switch (config.getMode()) {
      case AUTH:
        omnilockWitnessLock = signForAuthMode(transaction, scriptGroup, context.getKeyPair(), config);
        break;
      case ADMINISTRATOR:
        omnilockWitnessLock = signForAdministratorMode(transaction, scriptGroup, context.getKeyPair(), config);
        break;
      default:
        throw new IllegalArgumentException("Unknown Omnilock mode " + config.getMode());
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
  private static OmnilockWitnessLock signForAuthMode(Transaction transaction, ScriptGroup scriptGroup, ECKeyPair keyPair, Configuration configuration) {
    byte[] authArgs = Arrays.copyOfRange(scriptGroup.getScript().args, 1, 21);
    int firstIndex = scriptGroup.getInputIndices().get(0);
    byte[] firstWitness = transaction.witnesses.get(firstIndex);

    // witness placeholder should be set before signing
    WitnessArgs witnessArgs = WitnessArgs.unpack(firstWitness);
    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    switch (configuration.getOmnilockArgs().getAuthenticationArgs().getFlag()) {
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
        Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript = configuration.getMultisigScript();
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
        throw new IllegalArgumentException("Unknown auth flag " + configuration.getOmnilockArgs().getOmniArgs().getFlag());
    }
    return omnilockWitnessLock;
  }

  private static byte[] setSignatureToWitness(byte[] signatures, byte[] signature, Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript) {
    int offset = multisigScript.encode().length;
    for (int i = 0; i < multisigScript.getThreshold(); i++) {
      if (isEmpty(signatures, offset, 65)) {
        System.arraycopy(signature, 0, signatures, offset, 65);
        break;
      }
      offset += 65;
    }
    return signatures;
  }

  private static boolean isEmpty(byte[] lock, int offset, int length) {
    for (int i = offset; i < offset + length; i++) {
      if (lock[i] != 0) {
        return false;
      }
    }
    return true;
  }

  private static OmnilockWitnessLock signForAdministratorMode(Transaction transaction, ScriptGroup scriptGroup, ECKeyPair keyPair, Configuration configuration) {
    byte[] signature = null;
    switch (configuration.getOmnilockIdentity().getIdentity().getFlag()) {
      case CKB_SECP256K1_BLAKE160:
        throw new UnsupportedOperationException("CKB_SECP256K1_BLAKE160");
      case LOCK_SCRIPT_HASH:
        break;
      default:
    }
    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    omnilockWitnessLock.setOmnilockIdentity(configuration.getOmnilockIdentity());
    omnilockWitnessLock.setSignature(signature);
    return omnilockWitnessLock;
  }

  public static class Configuration {
    private OmnilockArgs omnilockArgs;
    private Mode mode;

    // For Auth mode with flag 0x06 (multisig)
    private Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript;

    // For Administrator mode
    private CellDep adminListCell;
    private OmnilockIdentity omnilockIdentity;

    public Configuration() {
    }

    public Configuration(OmnilockArgs omnilockArgs, Mode mode) {
      this.omnilockArgs = omnilockArgs;
      this.mode = mode;
    }

    public OmnilockArgs getOmnilockArgs() {
      return omnilockArgs;
    }

    public void setOmnilockArgs(OmnilockArgs omnilockArgs) {
      this.omnilockArgs = omnilockArgs;
    }

    public Mode getMode() {
      return mode;
    }

    public void setMode(Mode mode) {
      this.mode = mode;
    }

    public Secp256k1Blake160MultisigAllSigner.MultisigScript getMultisigScript() {
      return multisigScript;
    }

    public void setMultisigScript(Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript) {
      this.multisigScript = multisigScript;
    }

    public CellDep getAdminListCell() {
      return adminListCell;
    }

    public void setAdminListCell(CellDep adminListCell) {
      this.adminListCell = adminListCell;
    }

    public OmnilockIdentity getOmnilockIdentity() {
      return omnilockIdentity;
    }

    public void setOmnilockIdentity(OmnilockIdentity omnilockIdentity) {
      this.omnilockIdentity = omnilockIdentity;
    }

    public enum Mode {
      AUTH,
      ADMINISTRATOR
    }
  }
}
