package org.nervos.ckb.transaction;

import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class LockUtils {

  public static Script generateLockScriptWithPrivateKey(String privateKey, String codeHash) {
    String publicKey = ECKeyPair.publicKeyFromPrivate(privateKey);
    String blake160 =
        Numeric.prependHexPrefix(Numeric.cleanHexPrefix(Hash.blake2b(publicKey)).substring(0, 40));
    return new Script(codeHash, blake160, Script.TYPE);
  }

  public static Script generateLockScriptWithAddress(String address, String codeHash) {
    AddressParseResult addressParseResult = AddressParser.parse(address);
    return addressParseResult.script;
  }
}
