package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AddressParser extends AddressBaseOperator {

  private static String parsePayload(String address) {
    Bech32.Bech32Data parsed = Bech32.decode(address);
    byte[] data = convertBits(com.google.common.primitives.Bytes.asList(parsed.data), 5, 8, false);
    if (data.length == 0) {
      return null;
    }
    Bech32.Bech32Data bech32Data = new Bech32.Bech32Data(parsed.hrp, data);
    return Numeric.toHexStringNoPrefix(bech32Data.data);
  }

  public static AddressParseResult parse(String address) throws AddressFormatException {
    String payload = parsePayload(address);
    if (payload == null) {
      throw new AddressFormatException("Address bech32 decode fail");
    }
    String type = payload.substring(0, 2);
    if (TYPE_SHORT.equals(type)) {
      String codeHashIndex = payload.substring(2, 4);
      String args = Numeric.prependHexPrefix(payload.substring(4));
      if (Numeric.cleanHexPrefix(args).length() / 2 != 20) {
        throw new AddressFormatException("Short address args byte length must be equal to 20");
      }
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
        throw new AddressFormatException("Short address code hash index must be 00 or 01");
      }
    }

    if (payload.length() < 66) {
      throw new AddressFormatException("Invalid full address payload length");
    }
    String codeHash = Numeric.prependHexPrefix(payload.substring(2, 66));
    String args = Numeric.prependHexPrefix(payload.substring(66));
    if (TYPE_FULL_DATA.equals(type)) {
      return new AddressParseResult(
          parseNetwork(address),
          new Script(codeHash, args, Script.DATA),
          AddressParseResult.Type.FULL);
    } else if (TYPE_FULL_TYPE.equals(type)) {
      return new AddressParseResult(
          parseNetwork(address),
          new Script(codeHash, args, Script.TYPE),
          AddressParseResult.Type.FULL);
    }
    throw new AddressFormatException("Full address type must be 02 or 04");
  }

  public static Network parseNetwork(String address) {
    if (address.startsWith("ckb")) {
      return Network.MAINNET;
    } else if (address.startsWith("ckt")) {
      return Network.TESTNET;
    }
    throw new AddressFormatException("Address prefix should be ckb or ckt");
  }
}
