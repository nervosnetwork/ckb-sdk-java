package org.nervos.ckb.transaction;

import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

import java.util.Arrays;

public class LockUtils {

  public static Script generateLockScriptWithPrivateKey(String privateKey, byte[] codeHash) {
    byte[] publicKey = ECKeyPair.create(privateKey).getEncodedPublicKey(true);
    byte[] blake160 = Arrays.copyOfRange(Hash.blake160(publicKey), 0, 20);
    return new Script(codeHash, blake160, Script.HashType.TYPE);
  }

  public static Script generateLockScriptWithAddress(String address) {
    AddressParseResult addressParseResult = AddressParser.parse(address);
    return addressParseResult.script;
  }

  public static byte[] generateLockHashWithAddress(String address) {
    return generateLockScriptWithAddress(address).computeHash();
  }
}
