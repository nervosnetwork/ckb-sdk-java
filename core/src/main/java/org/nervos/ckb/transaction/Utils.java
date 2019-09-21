package org.nervos.ckb.transaction;

import java.io.IOException;
import java.util.Collections;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Utils {

  private static final String TESTNET_PREFIX = "ckt";

  static Script generateLockScriptWithPrivateKey(String privateKey, String codeHash) {
    String publicKey = ECKeyPair.publicKeyFromPrivate(privateKey);
    String blake160 =
        Numeric.prependHexPrefix(Numeric.cleanHexPrefix(Hash.blake2b(publicKey)).substring(0, 40));
    return new Script(codeHash, Collections.singletonList(blake160), Script.TYPE);
  }

  static Script generateLockScriptWithAddress(String address, String codeHash) {
    AddressUtils addressUtils =
        new AddressUtils(address.startsWith(TESTNET_PREFIX) ? Network.TESTNET : Network.MAINNET);
    String publicKeyBlake160 = addressUtils.getBlake160FromAddress(address);
    return new Script(codeHash, Collections.singletonList(publicKeyBlake160), Script.TYPE);
  }

  static SystemScriptCell getSystemScriptCell(CKBService ckbService) throws IOException {
    return SystemContract.getSystemScriptCell(ckbService);
  }
}
