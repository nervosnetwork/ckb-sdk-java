package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

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
    Network network = parseNetwork(address);
    if (TYPE_SHORT.equals(type)) {
      String codeHashIndex = payload.substring(2, 4);
      byte[] args = Numeric.hexStringToByteArray(payload.substring(4));
      if (!codeHashIndex.equals(CODE_HASH_IDX_ANYONE_CAN_PAY) && args.length != 20) {
        throw new AddressFormatException("Short address args byte length must be equal to 20");
      }
      switch (codeHashIndex) {
        case CODE_HASH_IDX_BLAKE160:
          return new AddressParseResult(
              network,
              new Script(
                  Numeric.hexStringToByteArray(SECP_BLAKE160_CODE_HASH),
                  args,
                  Script.HashType.TYPE),
              AddressParseResult.Type.SHORT);
        case CODE_HASH_IDX_MULTISIG:
          return new AddressParseResult(
              network,
              new Script(
                  Numeric.hexStringToByteArray(MULTISIG_CODE_HASH), args, Script.HashType.TYPE),
              AddressParseResult.Type.SHORT);
        case CODE_HASH_IDX_ANYONE_CAN_PAY:
          byte[] codeHash =
              Numeric.hexStringToByteArray(
                  network == Network.MAINNET ? ACP_MAINNET_CODE_HASH : ACP_TESTNET_CODE_HASH);
          return new AddressParseResult(
              network,
              new Script(codeHash, args, Script.HashType.TYPE),
              AddressParseResult.Type.SHORT);
        default:
          throw new AddressFormatException("Short address code hash index must be 00, 01 or 02");
      }
    }

    if (payload.length() < 66) {
      throw new AddressFormatException("Invalid full address payload length");
    }

    if (TYPE_FULL_DATA.equals(type)) {
      byte[] codeHash = Numeric.hexStringToByteArray(payload.substring(2, 66));
      byte[] args = Numeric.hexStringToByteArray(payload.substring(66));
      return new AddressParseResult(
          network, new Script(codeHash, args, Script.HashType.DATA), AddressParseResult.Type.FULL);
    } else if (TYPE_FULL_TYPE.equals(type)) {
      byte[] codeHash = Numeric.hexStringToByteArray(payload.substring(2, 66));
      byte[] args = Numeric.hexStringToByteArray(payload.substring(66));
      return new AddressParseResult(
          network, new Script(codeHash, args, Script.HashType.TYPE), AddressParseResult.Type.FULL);
    }

    if (TYPE_FULL_WITH_BECH32M.equals(type)) {
      byte[] codeHash = Numeric.hexStringToByteArray(payload.substring(2, 66));
      Script.HashType hashType = Serializer.deserializeHashType(payload.substring(66, 68));
      byte[] args = Numeric.hexStringToByteArray(payload.substring(68));

      return new AddressParseResult(
          network, new Script(codeHash, args, hashType), AddressParseResult.Type.FULL);
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
