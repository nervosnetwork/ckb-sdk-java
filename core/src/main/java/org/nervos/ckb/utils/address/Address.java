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