package org.nervos.ckb.transaction;

import java.util.Arrays;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class LockUtils {

  public static Script generateLockScriptWithPrivateKey(String privateKey, byte[] codeHash) {
    String publicKey = ECKeyPair.publicKeyFromPrivate(privateKey);
    byte[] blake160 =
        Arrays.copyOfRange(Numeric.hexStringToByteArray(Hash.blake160(publicKey)), 0, 20);
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
