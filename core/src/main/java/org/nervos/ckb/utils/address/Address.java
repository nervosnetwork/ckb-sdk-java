package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;

public class Address {
  Script script;
  Network network;

  public Address() {
  }

  public Address(Script script, Network network) {
    this.script = script;
    this.network = network;
  }

  public Script getScript() {
    return script;
  }

  public Address setScript(Script script) {
    Objects.requireNonNull(script);
    this.script = script;
    return this;
  }

  public Network getNetwork() {
    return network;
  }

  public Address setNetwork(Network network) {
    Objects.requireNonNull(network);
    this.network = network;
    return this;

  }

  public static Address decode(String address) {
    Network network = network(address.substring(0, 3));
    return decode(address, network);
  }

  private static Address decode(String address, Network network) {
    Objects.requireNonNull(address);
    byte[] payload = Bech32.decode(address).data;
    payload = convertBits(payload, 0, payload.length, 5, 8, false);
    switch (payload[0]) {
      case 0x00:
        return decodeLongBech32m(payload, network);
      case 0x01:
        return decodeShort(payload, network);
      case 0x02:
      case 0x04:
        return decodeLongBech32(payload, network);
      default:
        throw new AddressFormatException("Unknown format type");
    }
  }

  static final byte[] SECP256_BLAKE160_SIGNHASH_ALL_CODE_HASH =
      Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8");
  static final byte[] SECP256_BLAKE160_MULTISIG_ALL_CODE_HASH =
      Numeric.hexStringToByteArray("0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8");
  static final byte[] ANY_CAN_PAY_CODE_HASH_MAINNET =
      Numeric.hexStringToByteArray("0xd369597ff47f29fbc0d47d2e3775370d1250b85140c670e4718af712983a2354");
  static final byte[] ANY_CAN_PAY_CODE_HASH_TESTNET =
      Numeric.hexStringToByteArray("0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356");

  private static Address decodeShort(byte[] payload, Network network) {
    byte codeHashIndex = payload[1];

    byte[] codeHash;
    if (codeHashIndex == 0x00) {
      codeHash = SECP256_BLAKE160_SIGNHASH_ALL_CODE_HASH;
    } else if (codeHashIndex == 0x01) {
      codeHash = SECP256_BLAKE160_MULTISIG_ALL_CODE_HASH;
    } else if (codeHashIndex == 0x02) {
      if (network == Network.MAINNET) {
        codeHash = ANY_CAN_PAY_CODE_HASH_MAINNET;
      } else {
        codeHash = ANY_CAN_PAY_CODE_HASH_TESTNET;
      }
    } else {
      throw new AddressFormatException("Unknown code hash index");
    }
    byte[] args = Arrays.copyOfRange(payload, 2, payload.length);

    Script script = new Script(codeHash, args, Script.HashType.TYPE);
    return new Address(script, network);
  }

  private static Address decodeLongBech32(byte[] payload, Network network) {
    Script.HashType hashType;
    if (payload[0] == 0x04) {
      hashType = Script.HashType.TYPE;
    } else if (payload[0] == 0x02) {
      hashType = Script.HashType.DATA;
    } else {
      throw new AddressFormatException("Unknown script hash type");
    }
    byte[] codeHash = Arrays.copyOfRange(payload, 1, 33);
    byte[] args = Arrays.copyOfRange(payload, 33, payload.length);
    Script script = new Script(codeHash, args, hashType);
    return new Address(script, network);
  }

  private static Address decodeLongBech32m(byte[] payload, Network network) {
    byte[] codeHash = Arrays.copyOfRange(payload, 1, 33);
    Script.HashType hashType = Script.HashType.unpack(payload[33]);
    byte[] args = Arrays.copyOfRange(payload, 34, payload.length);
    Script script = new Script(codeHash, args, hashType);
    return new Address(script, network);
  }

  public String encode() {
    return encodeFullBech32m();
  }

  public String encodeShort() {
    byte[] payload = new byte[2 + script.args.length];
    byte codeHashIndex;
    byte[] codeHash = script.codeHash;
    if (Arrays.equals(codeHash, SECP256_BLAKE160_SIGNHASH_ALL_CODE_HASH)) {
      codeHashIndex = 0x00;
    } else if (Arrays.equals(codeHash, SECP256_BLAKE160_MULTISIG_ALL_CODE_HASH)) {
      codeHashIndex = 0x01;
    } else if ((network == Network.MAINNET && Arrays.equals(codeHash, ANY_CAN_PAY_CODE_HASH_MAINNET)
        || (network == Network.TESTNET && Arrays.equals(codeHash, ANY_CAN_PAY_CODE_HASH_TESTNET)))) {
      codeHashIndex = 0x02;
    } else {
      throw new AddressFormatException("Encoding to short address for given script is unsupported");
    }
    payload[0] = 0x01;
    payload[1] = codeHashIndex;
    System.arraycopy(script.args, 0, payload, 2, script.args.length);
    payload = convertBits(payload, 0, payload.length, 8, 5, true);
    return Bech32.encode(Bech32.Encoding.BECH32, hrp(network), payload);
  }

  public String encodeFullBech32() {
    byte[] payload = new byte[1 + script.codeHash.length + script.args.length];
    if (script.hashType == Script.HashType.TYPE) {
      payload[0] = 0x04;
    } else if (script.hashType == Script.HashType.DATA) {
      payload[1] = 0x02;
    } else {
      throw new AddressFormatException("Unknown script hash type");
    }
    int pos = 1;
    System.arraycopy(script.codeHash, 0, payload, pos, script.codeHash.length);
    pos += script.codeHash.length;
    System.arraycopy(script.args, 0, payload, pos, script.args.length);
    payload = convertBits(payload, 0, payload.length, 8, 5, true);
    return Bech32.encode(Bech32.Encoding.BECH32, hrp(network), payload);
  }

  public String encodeFullBech32m() {
    byte[] payload = new byte[1 + script.codeHash.length + 1 + script.args.length];
    payload[0] = 0x00;
    int pos = 1;
    System.arraycopy(script.codeHash, 0, payload, pos, script.codeHash.length);
    pos += script.codeHash.length;
    payload[pos] = script.hashType.pack();
    pos++;
    System.arraycopy(script.args, 0, payload, pos, script.args.length);
    payload = convertBits(payload, 0, payload.length, 8, 5, true);
    return Bech32.encode(Bech32.Encoding.BECH32M, hrp(network), payload);
  }

  private static Network network(String hrp) {
    switch (hrp) {
      case "ckb":
        return Network.MAINNET;
      case "ckt":
        return Network.TESTNET;
      default:
        throw new AddressFormatException("Invalid hrp");
    }
  }

  private static String hrp(Network network) {
    Objects.requireNonNull(network);
    switch (network) {
      case MAINNET:
        return "ckb";
      case TESTNET:
        return "ckt";
      default:
        throw new AddressFormatException("Unknown network");
    }
  }

  private static byte[] convertBits(final byte[] in, final int inStart, final int inLen, final int fromBits,
                                    final int toBits, final boolean pad) throws AddressFormatException {
    int acc = 0;
    int bits = 0;
    ByteArrayOutputStream out = new ByteArrayOutputStream(64);
    final int maxv = (1 << toBits) - 1;
    final int max_acc = (1 << (fromBits + toBits - 1)) - 1;
    for (int i = 0; i < inLen; i++) {
      int value = in[i + inStart] & 0xff;
      if ((value >>> fromBits) != 0) {
        throw new AddressFormatException(
            String.format("Input value '%X' exceeds '%d' bit size", value, fromBits));
      }
      acc = ((acc << fromBits) | value) & max_acc;
      bits += fromBits;
      while (bits >= toBits) {
        bits -= toBits;
        out.write((acc >>> bits) & maxv);
      }
    }
    if (pad) {
      if (bits > 0)
        out.write((acc << (toBits - bits)) & maxv);
    } else if (bits >= fromBits || ((acc << (toBits - bits)) & maxv) != 0) {
      throw new AddressFormatException("Could not convert bits, invalid padding");
    }
    return out.toByteArray();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Address address = (Address) o;

    if (!script.equals(address.script)) return false;
    return network == address.network;
  }

  @Override
  public int hashCode() {
    int result = script.hashCode();
    result = 31 * result + network.hashCode();
    return result;
  }
}