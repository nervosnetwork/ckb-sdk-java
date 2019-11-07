package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AddressGenerator extends AddressBaseOperator {

  public static String generate(Network network, Script script) {
    if (Script.TYPE.equals(script.hashType) && script.args.length() == 40) {
      if (SECP_BLAKE160_CODE_HASH.equals(Numeric.cleanHexPrefix(script.codeHash))) {
        // Payload: type(01) | code hash index(00, P2PH) | args
        String payload = TYPE_SHORT + CODE_HASH_IDX_BLAKE160 + Numeric.cleanHexPrefix(script.args);
        byte[] data = Numeric.hexStringToByteArray(payload);
        return Bech32.encode(
            prefix(network),
            convertBits(com.google.common.primitives.Bytes.asList(data), 8, 5, true));
      } else if (MULTISIG_CODE_HASH.equals(Numeric.cleanHexPrefix(script.codeHash))) {
        // Payload: type(01) | code hash index(01, multi-sig) | args
        String payload = TYPE_SHORT + CODE_HASH_IDX_MULTISIG + Numeric.cleanHexPrefix(script.args);
        byte[] data = Numeric.hexStringToByteArray(payload);
        return Bech32.encode(
            prefix(network),
            convertBits(com.google.common.primitives.Bytes.asList(data), 8, 5, true));
      } else {
        return generateFullAddress(network, script);
      }
    }
    return generateFullAddress(network, script);
  }

  public static String generateFullAddress(Network network, Script script) {
    String type = Script.TYPE.equals(script.hashType) ? TYPE_FULL_TYPE : TYPE_FULL_DATA;
    // Payload: type(02/04) | code hash | args
    String payload = type + script.codeHash + Numeric.cleanHexPrefix(script.args);
    byte[] data = Numeric.hexStringToByteArray(payload);
    return Bech32.encode(
        prefix(network), convertBits(com.google.common.primitives.Bytes.asList(data), 8, 5, true));
  }

  private static String prefix(Network network) {
    return network == Network.MAINNET ? "ckb" : "ckt";
  }
}
