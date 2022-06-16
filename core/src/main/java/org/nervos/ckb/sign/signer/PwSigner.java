package org.nervos.ckb.sign.signer;

import org.nervos.ckb.crypto.Keccak256;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.utils.MoleculeConverter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class PwSigner implements ScriptSigner {
  private static PwSigner INSTANCE;

  private PwSigner() {
  }

  public static PwSigner getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PwSigner();
    }
    return INSTANCE;
  }

  @Override
  public boolean signTransaction(
      Transaction transaction, ScriptGroup scriptGroup, Context context) {
    Script script = scriptGroup.getScript();
    ECKeyPair keyPair = context.getKeyPair();
    if (isMatched(keyPair, script.args)) {
      return signScriptGroup(transaction, scriptGroup, keyPair);
    } else {
      return false;
    }
  }

  private boolean signScriptGroup(
      Transaction transaction, ScriptGroup scriptGroup, ECKeyPair keyPair) {
    Keccak256 keccak256 = new Keccak256();
    byte[] txHash = transaction.computeHash();
    keccak256.update(txHash);

    List<byte[]> witnesses = transaction.witnesses;
    for (int i : scriptGroup.getInputIndices()) {
      byte[] witness = witnesses.get(i);
      keccak256.update(MoleculeConverter.packUint64(witness.length).toByteArray());
      keccak256.update(witness);
    }

    byte[] digest = keccak256.doFinal();
    byte[] signature = ethereumPersonalSign(digest, keyPair);

    int index = scriptGroup.getInputIndices().get(0);
    WitnessArgs witnessArgs = WitnessArgs.unpack(witnesses.get(index));
    witnessArgs.setLock(signature);
    witnesses.set(index, witnessArgs.pack().toByteArray());
    return true;
  }

  private static byte[] ethereumPersonalSign(byte[] message, ECKeyPair keyPair) {
    byte[] prefix =
        ("\u0019Ethereum Signed Message:\n" + message.length).getBytes(StandardCharsets.UTF_8);
    byte[] encodedMessage = new byte[prefix.length + message.length];
    System.arraycopy(prefix, 0, encodedMessage, 0, prefix.length);
    System.arraycopy(message, 0, encodedMessage, prefix.length, message.length);

    Keccak256 keccak256 = new Keccak256();
    keccak256.update(encodedMessage);
    byte[] digest = keccak256.doFinal();

    return Sign.signMessage(digest, keyPair).getSignature();
  }

  // Check if the script with `scriptArgs` is generated by and can be unlocked by `privateKey`
  public boolean isMatched(ECKeyPair keyPair, byte[] scriptArgs) {
    if (scriptArgs == null || keyPair == null) {
      return false;
    }
    Keccak256 keccak256 = new Keccak256();
    byte[] encodedPublicKey = keyPair.getEncodedPublicKey(false);
    keccak256.update(Arrays.copyOfRange(encodedPublicKey, 1, encodedPublicKey.length));
    byte[] publicKeyHash = keccak256.doFinal();

    byte[] ethereumAddress =
        Arrays.copyOfRange(publicKeyHash, publicKeyHash.length - 20, publicKeyHash.length);

    return Arrays.equals(scriptArgs, ethereumAddress);
  }
}
