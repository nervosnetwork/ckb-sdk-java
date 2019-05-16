package org.nervos.ckb.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;

public class Witness {
  public static List<String> generateWitness(BigInteger privateKey, String txHash) {
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    byte[] signatureBytes =
        Sign.signMessage(Numeric.hexStringToByteArray(txHash), ecKeyPair).getDerSignature();
    String signature = Numeric.toHexString(signatureBytes);
    List<String> witness = new ArrayList<>();
    witness.add(
        Numeric.toHexStringWithPrefixZeroPadded(Sign.publicKeyFromPrivate(privateKey, true), 66));
    witness.add(signature);
    witness.add(Numeric.littleEndian(signatureBytes.length));
    return witness;
  }
}
