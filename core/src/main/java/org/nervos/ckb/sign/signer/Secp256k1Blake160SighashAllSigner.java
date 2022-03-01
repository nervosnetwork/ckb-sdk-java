package org.nervos.ckb.sign.signer;

import java.util.Arrays;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;

public class Secp256k1Blake160SighashAllSigner implements ScriptSigner {
  private static final int WITNESS_OFFSET_IN_BYTE = 20;
  private static final int SIGNATURE_LENGTH_IN_BYTE = 65;

  private static Secp256k1Blake160SighashAllSigner INSTANCE;

  private Secp256k1Blake160SighashAllSigner() {}

  public static Secp256k1Blake160SighashAllSigner getINSTANCE() {
    if (INSTANCE == null) {
      INSTANCE = new Secp256k1Blake160SighashAllSigner();
    }
    return INSTANCE;
  }

  @Override
  public boolean signTransaction(
      Transaction transaction, ScriptGroup scriptGroup, Context context) {
    Script script = scriptGroup.getScript();
    String privateKey = context.getPrivateKey();
    if (isMatched(privateKey, script.args)) {
      return unlockScript(transaction, scriptGroup, privateKey);
    } else {
      return false;
    }
  }

  public boolean unlockScript(Transaction transaction, ScriptGroup scriptGroup, String privateKey) {
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);

    String txHash = transaction.computeHash();
    List<String> witnesses = transaction.witnesses;
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));

    for (int i : scriptGroup.getInputIndices()) {
      byte[] witness = Numeric.hexStringToByteArray(witnesses.get(i));
      blake2b.update(new UInt64(witness.length).toBytes());
      blake2b.update(witness);
    }
    for (int i = transaction.inputs.size(); i < transaction.witnesses.size(); i++) {
      byte[] witness = Numeric.hexStringToByteArray(witnesses.get(i));
      blake2b.update(new UInt64(witness.length).toBytes());
      blake2b.update(witness);
    }

    byte[] message = blake2b.doFinalBytes();
    byte[] signature = Sign.signMessage(message, ecKeyPair).getSignature();

    int index = scriptGroup.getInputIndices().get(0);
    String witness = Numeric.cleanHexPrefix(witnesses.get(index));
    witness =
        "0x"
            + witness.substring(0, WITNESS_OFFSET_IN_BYTE * 2)
            + Numeric.toHexStringNoPrefix(signature)
            + witness.substring(WITNESS_OFFSET_IN_BYTE * 2 + SIGNATURE_LENGTH_IN_BYTE * 2);

    witnesses.set(index, witness);
    return true;
  }

  // Check if the script with `scriptArgs` is generated by and can be unlocked by `privateKey`
  public boolean isMatched(String privateKey, String scriptArgs) {
    if (scriptArgs == null || privateKey == null) {
      return false;
    }
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, true);
    byte[] publicKeyHash = Hash.blake160(ecKeyPair.getPublicKeyBytes());
    byte[] scriptArgsBytes = Numeric.hexStringToByteArray(scriptArgs);
    return Arrays.equals(scriptArgsBytes, publicKeyHash);
  }
}
