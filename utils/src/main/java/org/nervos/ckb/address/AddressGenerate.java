package org.nervos.ckb.address;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Keys;
import org.nervos.ckb.utils.Numeric;

/** @author zjh @Created Date: 2021/7/15 @Description: @Modify by: */
public class AddressGenerate {

  public AddressGenerateResult generateShortAddress(Network network)
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    ECKeyPair ecKeyPair = Keys.createEcKeyPair();

    String privateKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
    String publicKey = ECKeyPair.publicKeyFromPrivate(privateKey);
    String blake160 = Hash.blake160(publicKey);

    AddressUtils utils = new AddressUtils(network, CodeHashType.BLAKE160);
    String addresss = utils.generate(blake160);

    AddressGenerateResult result = new AddressGenerateResult();
    result.address = addresss;
    result.lockArgs = blake160;
    result.privateKey = privateKey;

    return result;
  }

  public static class AddressGenerateResult {

    public String address;

    public String lockArgs;

    public String privateKey;

    @Override
    public String toString() {
      return "AddressGenerateResult{"
          + "address='"
          + address
          + '\''
          + ", lockArgs='"
          + lockArgs
          + '\''
          + ", privateKey='"
          + privateKey
          + '\''
          + '}';
    }
  }
}
