package org.nervos.ckb.utils.address;

import com.google.common.primitives.Bytes;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Bech32m;
import org.nervos.ckb.utils.Numeric;

import java.util.Arrays;
import java.util.List;

public class AddressGenerator extends AddressBaseOperator {
  private static final List<String> codeHashes =
      Arrays.asList(
          SECP_BLAKE160_CODE_HASH,
          MULTISIG_CODE_HASH,
          ACP_MAINNET_CODE_HASH,
          ACP_TESTNET_CODE_HASH);

  public static String generate(Network network, Script script) {
    return generateBech32mFullAddress(network, script);
  }

  /**
   * Short address format is deprecated because it is limited (only supports secp256k1_blake160,
   * secp256k1_multisig, anyone_can_pay) and a flaw has been found in its encoding method bech32,
   * which could enable attackers to generate valid but unexpected addresses. For more please check
   * https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md
   */
  @Deprecated
  public static String generateShortAddress(Network network, Script script) {
    // Payload: type(01) | code hash index(00, P2PH / 01, multi-sig / 02, anyone_can_pay) | args
    String codeHash = Numeric.toHexStringNoPrefix(script.codeHash);
    String args = Numeric.toHexStringNoPrefix(script.args);

    // Throw exception if short address can be converted from the given script
    if (!(Script.HashType.TYPE.equals(script.hashType)
        && args.length() >= 40
        && args.length() <= 44
        && codeHashes.contains(codeHash))) {
      throw new UnsupportedOperationException(
          "The given script can not be converted into short address. Unsupported.");
    }

    String codeHashIndex = "";
    if (ACP_MAINNET_CODE_HASH.equals(codeHash) || ACP_TESTNET_CODE_HASH.equals(codeHash)) {
      codeHashIndex = CODE_HASH_IDX_ANYONE_CAN_PAY;
    } else if (args.length() == 40) {
      if (SECP_BLAKE160_CODE_HASH.equals(codeHash)) {
        codeHashIndex = CODE_HASH_IDX_BLAKE160;
      } else if (MULTISIG_CODE_HASH.equals(codeHash)) {
        codeHashIndex = CODE_HASH_IDX_MULTISIG;
      }
    }
    if (codeHashIndex.isEmpty()) {
      throw new AddressFormatException("Code hash index of address format error");
    }

    String payload = TYPE_SHORT + codeHashIndex + args;
    byte[] data = Numeric.hexStringToByteArray(payload);
    return Bech32.encode(
        prefix(network), convertBits(com.google.common.primitives.Bytes.asList(data), 8, 5, true));
  }

  /**
   * Old full address format is deprecated because a flaw has been found in its encoding method
   * bech32, which could enable attackers to generate valid but unexpected addresses. For more
   * please check
   * https://github.com/nervosnetwork/rfcs/blob/master/rfcs/0021-ckb-address-format/0021-ckb-address-format.md
   */
  @Deprecated
  public static String generateFullAddress(Network network, Script script) {
    String type = Script.HashType.TYPE.equals(script.hashType) ? TYPE_FULL_TYPE : TYPE_FULL_DATA;
    // Payload: type(02/04) | code hash | args
    String payload =
        type
            + Numeric.toHexStringNoPrefix(script.codeHash)
            + Numeric.toHexStringNoPrefix(script.args);
    byte[] data = Numeric.hexStringToByteArray(payload);
    return Bech32.encode(
        prefix(network), convertBits(com.google.common.primitives.Bytes.asList(data), 8, 5, true));
  }

  public static String generateBech32mFullAddress(Network network, Script script) {
    // Payload: type(00) | code hash | hash type | args

    byte[] payload =
        Numeric.hexStringToByteArray(
            TYPE_FULL_WITH_BECH32M
                + Numeric.toHexStringNoPrefix(script.codeHash)
                + String.format("%02x", script.hashType.pack())
                + Numeric.toHexStringNoPrefix(script.args));

    byte[] data_part = convertBits(Bytes.asList(payload), 8, 5, true);
    return Bech32m.encode(prefix(network), data_part);
  }

  private static String prefix(Network network) {
    return network == Network.MAINNET ? "ckb" : "ckt";
  }
}
