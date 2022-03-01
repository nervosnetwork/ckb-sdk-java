package org.nervos.ckb.unlocker.script;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.nervos.ckb.crypto.Keccak256;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.unlocker.Context;
import org.nervos.ckb.unlocker.ScriptGroup;
import org.nervos.ckb.unlocker.ScriptUnlocker;
import org.nervos.ckb.utils.Numeric;

public class PwUnlocker implements ScriptUnlocker {
  private static final int WITNESS_OFFSET_IN_BYTE = 20;
  private static final int SIGNATURE_LENGTH_IN_BYTE = 65;

  private static PwUnlocker instance;

  private PwUnlocker() {}

  public static PwUnlocker getInstance() {
    if (instance == null) {
      instance = new PwUnlocker();
    }
    return instance;
  }

  @Override
  public boolean unlockScript(Transaction transaction, ScriptGroup scriptGroup, Context context) {
    Script script = scriptGroup.getScript();
    String privateKey = context.getPrivateKey();
    return unlockEthereum(transaction, scriptGroup, privateKey);
  }

  public boolean unlockEthereum(
      Transaction transaction, ScriptGroup scriptGroup, String privateKey) {
    Keccak256 keccak256 = new Keccak256();
    String txHash = transaction.computeHash();
    keccak256.update(Numeric.hexStringToByteArray(txHash));

    List<String> witnesses = transaction.witnesses;
    for (int i : scriptGroup.getInputIndices()) {
      byte[] witness = Numeric.hexStringToByteArray(witnesses.get(i));
      keccak256.update(new UInt64(witness.length).toBytes());
      keccak256.update(witness);
    }

    byte[] digest = keccak256.doFinalBytes();

    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    byte[] signature = ethereumPersonalSign(digest, ecKeyPair);

    int index = scriptGroup.getInputIndices().get(0);
    String witness = witnesses.get(index);
    witness =
        "0x"
            + witness.substring(0, WITNESS_OFFSET_IN_BYTE * 2)
            + Numeric.toHexStringNoPrefix(signature)
            + witness.substring(WITNESS_OFFSET_IN_BYTE * 2 + SIGNATURE_LENGTH_IN_BYTE * 2);

    witnesses.set(index, witness);
    return true;
  }

  private static byte[] ethereumPersonalSign(byte[] message, ECKeyPair ecKeyPair) {
    byte[] prefix =
        ("\u0019Ethereum Signed Message:\n" + message.length).getBytes(StandardCharsets.UTF_8);
    byte[] encodedMessage = new byte[prefix.length + message.length];
    System.arraycopy(prefix, 0, encodedMessage, 0, prefix.length);
    System.arraycopy(message, 0, encodedMessage, prefix.length, message.length);

    Keccak256 keccak256 = new Keccak256();
    keccak256.update(encodedMessage);
    byte[] digest = keccak256.doFinalBytes();

    return Sign.signMessage(digest, ecKeyPair).getSignature();
  }
}
