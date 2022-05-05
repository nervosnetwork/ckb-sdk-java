package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;

import java.io.ByteArrayOutputStream;
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

  private static Address decodeShort(byte[] payload, Network network) {
    return null;
  }

  private static Address decodeLongBech32(byte[] payload, Network network) {
    return null;
  }

  private static Address decodeLongBech32m(byte[] payload, Network network) {
    return null;
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
}