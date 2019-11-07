package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AddressParser extends AddressBaseOperator {

  private static String parsePrefix(String address) {
    Bech32.Bech32Data parsed = Bech32.decode(address);
    byte[] data = convertBits(com.google.common.primitives.Bytes.asList(parsed.data), 5, 8, false);
    if (data.length == 0) {
      return null;
    }
    Bech32.Bech32Data bech32Data = new Bech32.Bech32Data(parsed.hrp, data);
    return Numeric.toHexStringNoPrefix(bech32Data.data);
  }

  public static AddressParseResult parse(String address) throws AddressFormatException {
    String payload = parsePrefix(address);
    if (payload == null) {
      throw new AddressFormatException("Address parse fail");
    }
    String type = payload.substring(0, 2);
    if (TYPE_SHORT.equals(type)) {
      String codeHashIndex = payload.substring(2, 4);
      String args = Numeric.prependHexPrefix(payload.substring(4));
      if (CODE_HASH_IDX_BLAKE160.equals(codeHashIndex)) {
        return new AddressParseResult(
            parseNetwork(address),
            new Script(Numeric.prependHexPrefix(SECP_BLAKE160_CODE_HASH), args, Script.TYPE),
            AddressParseResult.Type.SHORT);
      } else if (CODE_HASH_IDX_MULTISIG.equals(codeHashIndex)) {
        return new AddressParseResult(
            parseNetwork(address),
            new Script(Numeric.prependHexPrefix(MULTISIG_CODE_HASH), args, Script.TYPE),
            AddressParseResult.Type.SHORT);
      } else {
        throw new AddressFormatException("Address parse fail");
      }
    }

    String codeHash = Numeric.prependHexPrefix(payload.substring(2, 66));
    String args = Numeric.prependHexPrefix(payload.substring(66));
    if (TYPE_FULL_DATA.equals(type)) {
      return new AddressParseResult(
          parseNetwork(address),
          new Script(codeHash, args, Script.DATA),
          AddressParseResult.Type.FULL);
    }
    return new AddressParseResult(
        parseNetwork(address),
        new Script(codeHash, args, Script.TYPE),
        AddressParseResult.Type.FULL);
  }

  private static Network parseNetwork(String address) {
    return address.startsWith("ckb") ? Network.MAINNET : Network.TESTNET;
  }
}
