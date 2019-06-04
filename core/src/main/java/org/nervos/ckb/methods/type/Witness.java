package org.nervos.ckb.methods.type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.utils.Numeric;

public class Witness {
  public List<String> data = new ArrayList<>();

  public Witness() {}

  public Witness(List<String> data) {
    this.data = data;
  }

  public Witness(BigInteger privateKey, String txHash) {
    List<String> oldData = data;
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));
    for (String datum : data) {
      blake2b.update(Numeric.hexStringToByteArray(datum));
    }
    String message = blake2b.doFinalString();

    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    String publicKey =
        Numeric.toHexStringWithPrefixZeroPadded(Sign.publicKeyFromPrivate(privateKey, true), 66);
    String signature =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getDerSignature());
    data = new ArrayList<>();
    data.add(publicKey);
    data.add(signature);
    data.addAll(oldData);
  }
}
