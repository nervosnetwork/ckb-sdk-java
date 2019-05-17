package org.nervos.ckb.methods.type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.utils.Numeric;

public class Witness {
  public List<String> data;

  public Witness(List<String> data) {
    this.data = data;
  }

  public Witness(BigInteger privateKey, String txHash) {
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    byte[] signatureBytes =
        Sign.signMessage(Numeric.hexStringToByteArray(txHash), ecKeyPair).getDerSignature();
    String signature = Numeric.toHexString(signatureBytes);
    data = new ArrayList<>();
    data.add(
        Numeric.toHexStringWithPrefixZeroPadded(Sign.publicKeyFromPrivate(privateKey, true), 66));
    data.add(signature);
    data.add(Numeric.littleEndian(signatureBytes.length));
  }
}
