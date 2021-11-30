package org.nervos.ckb.utils.address;

import com.google.common.primitives.Bytes;
import java.util.Arrays;
import java.util.List;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Bech32m;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
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

  @Deprecated
  public static String generateShortAddress(Network network, Script script) {
    // Payload: type(01) | code hash index(00, P2PH / 01, multi-sig / 02, anyone_can_pay) | args
    String codeHash = Numeric.cleanHexPrefix(script.codeHash);
    String args = Numeric.cleanHexPrefix(script.args);

    // Throw exception if short address can be converted from the given script
    if (!(Script.TYPE.equals(script.hashType)
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

  @Deprecated
  public static String generateFullAddress(Network network, Script script) {
    String type = Script.TYPE.equals(script.hashType) ? TYPE_FULL_TYPE : TYPE_FULL_DATA;
    // Payload: type(02/04) | code hash | args
    String payload =
        type + Numeric.cleanHexPrefix(script.codeHash) + Numeric.cleanHexPrefix(script.args);
    byte[] data = Numeric.hexStringToByteArray(payload);
    return Bech32.encode(
        prefix(network), convertBits(com.google.common.primitives.Bytes.asList(data), 8, 5, true));
  }

  public static String generateBech32mFullAddress(Network network, Script script) {
    // Payload: type(00) | code hash | hash type | args

    byte[] payload =
        Numeric.hexStringToByteArray(
            TYPE_FULL_WITH_BECH32M
                + Numeric.cleanHexPrefix(script.codeHash)
                + Serializer.serializeHashType(script.hashType)
                + Numeric.cleanHexPrefix(script.args));

    byte[] data_part = convertBits(Bytes.asList(payload), 8, 5, true);
    return Bech32m.encode(prefix(network), data_part);
  }

  private static String prefix(Network network) {
    return network == Network.MAINNET ? "ckb" : "ckt";
  }
}
