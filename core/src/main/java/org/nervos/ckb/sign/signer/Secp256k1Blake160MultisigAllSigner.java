package org.nervos.ckb.sign.signer;

import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.utils.MoleculeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Secp256k1Blake160MultisigAllSigner implements ScriptSigner {
  private static final int SIGNATURE_LENGTH_IN_BYTE = 65;

  private static Secp256k1Blake160MultisigAllSigner INSTANCE;

  private Secp256k1Blake160MultisigAllSigner() {
  }

  public static Secp256k1Blake160MultisigAllSigner getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Secp256k1Blake160MultisigAllSigner();
    }
    return INSTANCE;
  }

  @Override
  public boolean signTransaction(
      Transaction transaction, ScriptGroup scriptGroup, Context context) {
    Script script = scriptGroup.getScript();
    ECKeyPair keyPair = context.getKeyPair();
    Object payload = context.getPayload();
    if (payload instanceof MultisigScript) {
      MultisigScript multisigScript = (MultisigScript) payload;
      if (isMatched(keyPair, script.args, multisigScript)) {
        return signScriptGroup(transaction, scriptGroup, keyPair, multisigScript);
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public boolean signScriptGroup(
      Transaction transaction, ScriptGroup scriptGroup, ECKeyPair keyPair, MultisigScript multisigScript) {
    byte[] txHash = transaction.computeHash();
    List<byte[]> witnesses = transaction.witnesses;
    Blake2b blake2b = new Blake2b();
    blake2b.update(txHash);

    int firstIndex = scriptGroup.getInputIndices().get(0);
    byte[] firstWitness = witnesses.get(firstIndex);
    byte[] firstWitnessPlaceholder = recoverFirstWitnessPlaceHolder(firstWitness, multisigScript);
    witnesses.set(firstIndex, firstWitnessPlaceholder);

    for (int i : scriptGroup.getInputIndices()) {
      byte[] witness = witnesses.get(i);
      blake2b.update(MoleculeConverter.packUint64(witness.length).toByteArray());
      blake2b.update(witness);
    }
    for (int i = transaction.inputs.size(); i < transaction.witnesses.size(); i++) {
      byte[] witness = witnesses.get(i);
      blake2b.update(MoleculeConverter.packUint64(witness.length).toByteArray());
      blake2b.update(witness);
    }

    byte[] message = blake2b.doFinal();
    byte[] signature = Sign.signMessage(message, keyPair).getSignature();

    firstWitness = setSignatureToWitness(firstWitness, signature, multisigScript);
    witnesses.set(firstIndex, firstWitness);
    return true;
  }

  private static byte[] recoverFirstWitnessPlaceHolder(byte[] originalWitness, MultisigScript multisigScript) {
    WitnessArgs witnessArgs = WitnessArgs.unpack(originalWitness);
    byte[] header = multisigScript.encode();
    byte[] lockPlaceHolder = new byte[header.length + SIGNATURE_LENGTH_IN_BYTE * multisigScript.getThreshold()];
    System.arraycopy(header, 0, lockPlaceHolder, 0, header.length);
    witnessArgs.setLock(lockPlaceHolder);
    return witnessArgs.pack().toByteArray();
  }

  private static byte[] setSignatureToWitness(byte[] witness, byte[] signature, MultisigScript multisigScript) {
    WitnessArgs witnessArgs = WitnessArgs.unpack(witness);
    byte[] lock = witnessArgs.getLock();
    int pos = multisigScript.encode().length;
    for (int i = 0; i < multisigScript.getThreshold(); i++) {
      if (isEmptySignature(lock, pos)) {
        System.arraycopy(signature, 0, lock, pos, SIGNATURE_LENGTH_IN_BYTE);
        break;
      }
      pos += SIGNATURE_LENGTH_IN_BYTE;
    }
    witnessArgs.setLock(lock);
    return witnessArgs.pack().toByteArray();
  }

  private static boolean isEmptySignature(byte[] lock, int start) {
    for (int i = 0; i < SIGNATURE_LENGTH_IN_BYTE; i++) {
      if (lock[start + i] != 0) {
        return false;
      }
    }
    return true;
  }

  private static boolean isMatched(ECKeyPair keyPair, byte[] scriptArgs, MultisigScript multisigScript) {
    if (scriptArgs == null || keyPair == null) {
      return false;
    }
    return Arrays.equals(scriptArgs, multisigScript.getArgs());
  }

  public static class MultisigScript {
    private int version = 0x0;
    private int firstN = 0;
    private int threshold;
    private List<byte[]> keysHashes;

    private MultisigScript() {
    }

    public MultisigScript(int version, int firstN, int threshold, List<byte[]> keysHashes) {
      if (firstN < 0) {
        throw new IllegalArgumentException("FirstN must be greater than or equal to 0");
      }
      if (keysHashes.size() == 0) {
        throw new IllegalArgumentException("Public key hashes must not be empty");
      }
      if (keysHashes.size() < threshold) {
        throw new IllegalArgumentException("Size of public key hashes must be greater than or equal to threshold");
      }
      if (firstN > threshold) {
        throw new IllegalArgumentException("The firstN must be less than or equal to threshold");
      }
      this.version = version;
      this.firstN = firstN;
      this.threshold = threshold;
      this.keysHashes = keysHashes;
    }

    public MultisigScript(int firstN, int threshold, List<byte[]> keysHashes) {
      this(0, 0, threshold, keysHashes);
    }

    public MultisigScript(int threshold, List<byte[]> keysHashes) {
      this(0, 0, threshold, keysHashes);
    }

    public int getVersion() {
      return version;
    }

    public int getFirstN() {
      return firstN;
    }

    public int getThreshold() {
      return threshold;
    }

    public List<byte[]> getKeysHashes() {
      return keysHashes;
    }

    public byte[] encode() {
      byte[] out = new byte[4 + this.keysHashes.size() * 20];
      out[0] = (byte) this.version;
      out[1] = (byte) this.firstN;
      out[2] = (byte) this.threshold;
      out[3] = (byte) this.keysHashes.size();
      int pos = 4;
      for (byte[] publicKeyHash : this.keysHashes) {
        System.arraycopy(publicKeyHash, 0, out, pos, 20);
        pos += 20;
      }
      return out;
    }

    public static MultisigScript decode(byte[] in) {
      if (in.length < 24) {
        throw new IllegalArgumentException("bytes length should be greater than 24");
      }
      if ((in.length - 4) % 20 != 0) {
        throw new IllegalArgumentException("Invalid bytes length");
      }
      // round up
      if ((in.length - 4 + 10) / 20 != in[3]) {
        throw new IllegalArgumentException("Invalid public key list size");
      }
      MultisigScript multisigScript = new MultisigScript();
      multisigScript.version = in[0];
      multisigScript.firstN = in[1];
      multisigScript.threshold = in[2];
      multisigScript.keysHashes = new ArrayList<>();
      for (int i = 0; i < in[3]; i++) {
        byte[] publicKeyHash = new byte[20];
        System.arraycopy(in, 4 + i * 20, publicKeyHash, 0, 20);
        multisigScript.keysHashes.add(publicKeyHash);
      }
      return multisigScript;
    }

    public byte[] getArgs() {
      byte[] hash = Blake2b.digest(encode());
      return Arrays.copyOfRange(hash, 0, 20);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      MultisigScript multisigScript = (MultisigScript) o;

      if (version != multisigScript.version) return false;
      if (firstN != multisigScript.firstN) return false;
      if (threshold != multisigScript.threshold) return false;

      if (keysHashes.size() != multisigScript.keysHashes.size()) return false;
      for (int i = 0; i < keysHashes.size(); i++) {
        if (!Arrays.equals(keysHashes.get(i), multisigScript.keysHashes.get(i))) {
          return false;
        }
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = version;
      result = 31 * result + firstN;
      result = 31 * result + threshold;
      result = 31 * result + keysHashes.hashCode();
      return result;
    }
  }
}
