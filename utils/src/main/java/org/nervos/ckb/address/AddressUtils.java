package org.nervos.ckb.address;

import com.google.common.primitives.Bytes;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Numeric;

/**
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 *
 * <p>AddressUtils based on CKB Address Format
 * [RFC](https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md),
 * and [Common Address Format](https://github.com/nervosnetwork/ckb/wiki/Common-Address-Format).
 * Currently we implement the predefined format for type 0x01 and code hash index 0x00.
 */
public class AddressUtils {
  private static final String TYPE = "01"; // short address format
  private static final String CODE_HASH_IDX_BLAKE160 = "00";
  private static final String CODE_HASH_IDX_MULTISIG = "01";
  private static final String CODE_HASH_IDX_ACP = "02";

  private Network network;
  private CodeHashType codeHashType;

  public AddressUtils(Network network, CodeHashType codeHashType) {
    this.network = network;
    this.codeHashType = codeHashType;
  }

  public AddressUtils(Network network) {
    this.network = network;
    this.codeHashType = CodeHashType.BLAKE160;
  }

  private String getCodeHashIdx() throws AddressFormatException {
    switch (codeHashType) {
      case BLAKE160:
        return CODE_HASH_IDX_BLAKE160;
      case MULTISIG:
        return CODE_HASH_IDX_MULTISIG;
      case ANYONE_CAN_APY:
        return CODE_HASH_IDX_ACP;
      default:
        throw new AddressFormatException("Code hash index error");
    }
  }

  public String generateFromPublicKey(String publicKey) throws AddressFormatException {
    return generate(Hash.blake160(publicKey));
  }

  public String generate(String args) throws AddressFormatException {
    // Payload: type(01) | code hash index(00, P2PH /01, multi sig /02, ACP) | args
    String payload = TYPE + getCodeHashIdx() + Numeric.cleanHexPrefix(args);
    byte[] data = Numeric.hexStringToByteArray(payload);
    return Bech32.encode(prefix(), convertBits(Bytes.asList(data), 8, 5, true));
  }

  private static String parsePrefix(String address) {
    Bech32.Bech32Data parsed = Bech32.decode(address);
    byte[] data = convertBits(Bytes.asList(parsed.data), 5, 8, false);
    if (data.length == 0) {
      return null;
    }
    Bech32.Bech32Data bech32Data = new Bech32.Bech32Data(parsed.hrp, data);
    return Numeric.toHexStringNoPrefix(bech32Data.data);
  }

  public static CodeHashType parseAddressType(String address) throws AddressFormatException {
    String payload = parsePrefix(address);
    String prefixCodeHash = payload.substring(TYPE.length());
    if (prefixCodeHash.startsWith(CODE_HASH_IDX_BLAKE160)) {
      return CodeHashType.BLAKE160;
    }
    return CodeHashType.MULTISIG;
  }

  public static String parse(String address) throws AddressFormatException {
    String payload = parsePrefix(address);
    String prefixCodeHash = payload.substring(TYPE.length());
    System.out.println(payload);
    if (prefixCodeHash.startsWith(CODE_HASH_IDX_BLAKE160)) {
      return payload.substring((TYPE + CODE_HASH_IDX_BLAKE160).length());
    }
    return payload.substring((TYPE + CODE_HASH_IDX_MULTISIG).length());
  }

  private String prefix() {
    return network == Network.MAINNET ? "ckb" : "ckt";
  }

  public String strToAscii(String value) {
    StringBuilder sb = new StringBuilder();
    char[] chars = value.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      sb.append(Integer.toHexString((int) chars[i]));
    }
    return sb.toString();
  }

  public static byte[] convertBits(List<Byte> data, int fromBits, int toBits, boolean pad)
      throws AddressFormatException {
    int acc = 0;
    int bits = 0;
    int maxv = (1 << toBits) - 1;
    List<Byte> ret = new ArrayList<>();
    for (Byte value : data) {
      short b = (short) (value & 0xff);
      if ((b >> fromBits) > 0) {
        throw new AddressFormatException();
      }
      acc = (acc << fromBits) | b;
      bits += fromBits;
      while (bits >= toBits) {
        bits -= toBits;
        ret.add((byte) ((acc >> bits) & maxv));
      }
    }
    if (pad && (bits > 0)) {
      ret.add((byte) ((acc << (toBits - bits)) & maxv));
    } else if (bits >= fromBits || (byte) (((acc << (toBits - bits)) & maxv)) != 0) {
      throw new AddressFormatException(
          "Strict mode was used but input couldn't be converted without padding");
    }
    return Bytes.toArray(ret);
  }
}
