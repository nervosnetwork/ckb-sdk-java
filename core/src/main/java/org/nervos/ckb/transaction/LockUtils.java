package org.nervos.ckb.transaction;

import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.address.Address;

import java.util.Arrays;

public class LockUtils {

  public static Script generateLockScriptWithPrivateKey(String privateKey, byte[] codeHash) {
    byte[] publicKey = ECKeyPair.create(privateKey).getEncodedPublicKey(true);
    byte[] blake160 = Arrays.copyOfRange(Blake2b.digest(publicKey), 0, 20);
    return new Script(codeHash, blake160, Script.HashType.TYPE);
  }

  public static Script generateLockScriptWithAddress(String address) {
    return Address.decode(address).getScript();
  }

  public static byte[] generateLockHashWithAddress(String address) {
    return generateLockScriptWithAddress(address).computeHash();
  }
}
