package org.nervos.ckb.utils.address;

import com.google.common.primitives.Bytes;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.exceptions.AddressFormatException;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
class AddressBaseOperator {
  static final String TYPE_SHORT = "01"; // short version for locks with popular code_hash
  static final String TYPE_FULL_DATA = "02"; // full version with hash_type = "Data"
  static final String TYPE_FULL_TYPE = "04"; // full version with hash_type = "Type"
  static final String TYPE_FULL_WITH_BECH32M = "00"; // full version with bech32m

  static final String CODE_HASH_IDX_BLAKE160 = "00";
  static final String CODE_HASH_IDX_MULTISIG = "01";
  static final String CODE_HASH_IDX_ANYONE_CAN_PAY = "02";

  static final String SECP_BLAKE160_CODE_HASH =
      "9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8";
  static final String MULTISIG_CODE_HASH =
      "5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8";
  static final String ACP_MAINNET_CODE_HASH =
      "d369597ff47f29fbc0d47d2e3775370d1250b85140c670e4718af712983a2354";
  static final String ACP_TESTNET_CODE_HASH =
      "3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356";

  static byte[] convertBits(List<Byte> data, int fromBits, int toBits, boolean pad)
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
