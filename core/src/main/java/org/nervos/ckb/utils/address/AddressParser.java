package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AddressParser extends AddressBaseOperator {
  public static AddressParseResult parse(String address) throws AddressFormatException {
    Bech32.Bech32Data parsed = Bech32.decode(address);
    byte[] data = convertBits(com.google.common.primitives.Bytes.asList(parsed.data), 5, 8, false);
    String payload = Numeric.toHexStringNoPrefix(data);
    if (payload == null) {
      throw new AddressFormatException("Address bech32 decode fail");
    }
    String type = payload.substring(0, 2);
    Network network = parseNetwork(address);
    if (TYPE_SHORT.equals(type)) {
      if (parsed.encoding != Bech32.Encoding.BECH32) {
        throw new AddressFormatException("payload header 0x01 should have encoding BECH32");
      }
      String codeHashIndex = payload.substring(2, 4);
      String args = Numeric.prependHexPrefix(payload.substring(4));
      if (!codeHashIndex.equals(CODE_HASH_IDX_ANYONE_CAN_PAY)
          && Numeric.cleanHexPrefix(args).length() / 2 != 20) {
        throw new AddressFormatException("Short address args byte length must be equal to 20");
      }
      switch (codeHashIndex) {
        case CODE_HASH_IDX_BLAKE160:
          if (data.length != 22) {
            throw new AddressFormatException("payload bytes length of secp256k1-sighash-all "
                                                 + "short address should be 22");
          }
          return new AddressParseResult(
              network,
              new Script(Numeric.prependHexPrefix(SECP_BLAKE160_CODE_HASH), args, Script.TYPE),
              AddressParseResult.Type.SHORT);
        case CODE_HASH_IDX_MULTISIG:
          if (data.length != 22) {
            throw new AddressFormatException("payload bytes length of secp256k1-multisig-all "
                                                 + "short address should be 22");
          }
          return new AddressParseResult(
              network,
              new Script(Numeric.prependHexPrefix(MULTISIG_CODE_HASH), args, Script.TYPE),
              AddressParseResult.Type.SHORT);
        case CODE_HASH_IDX_ANYONE_CAN_PAY:
          if (data.length < 22 || data.length > 24) {
            throw new AddressFormatException("payload bytes length of anyone-can-pay "
                                                 + "short address should between 22 and 24");
          }
          String codeHash =
              Numeric.prependHexPrefix(
                  network == Network.MAINNET ? ACP_MAINNET_CODE_HASH : ACP_TESTNET_CODE_HASH);
          return new AddressParseResult(
              network, new Script(codeHash, args, Script.TYPE), AddressParseResult.Type.SHORT);
        default:
          throw new AddressFormatException("Short address code hash index must be 00, 01 or 02");
      }
    }

    if (payload.length() < 66) {
      throw new AddressFormatException("Invalid full address payload length");
    }

    if (TYPE_FULL_DATA.equals(type)) {
      if (parsed.encoding != Bech32.Encoding.BECH32) {
        throw new AddressFormatException("payload header 0x02 should have encoding BECH32");
      }
      String codeHash = Numeric.prependHexPrefix(payload.substring(2, 66));
      String args = Numeric.prependHexPrefix(payload.substring(66));
      return new AddressParseResult(
          network, new Script(codeHash, args, Script.DATA), AddressParseResult.Type.FULL);
    } else if (TYPE_FULL_TYPE.equals(type)) {
      if (parsed.encoding != Bech32.Encoding.BECH32) {
        throw new AddressFormatException("payload header 0x04 should have encoding BECH32");
      }
      String codeHash = Numeric.prependHexPrefix(payload.substring(2, 66));
      String args = Numeric.prependHexPrefix(payload.substring(66));
      return new AddressParseResult(
          network, new Script(codeHash, args, Script.TYPE), AddressParseResult.Type.FULL);
    }

    if (TYPE_FULL_WITH_BECH32M.equals(type)) {
      if (parsed.encoding != Bech32.Encoding.BECH32M) {
        throw new AddressFormatException("payload header 0x01 should have encoding BECH32M");
      }
      String codeHash = Numeric.prependHexPrefix(payload.substring(2, 66));
      String hashType = Serializer.deserializeHashType(payload.substring(66, 68));
      String args = Numeric.prependHexPrefix(payload.substring(68));

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
