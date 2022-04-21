package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.utils.Utils;

import static org.nervos.ckb.utils.MoleculeConverter.packByte32;
import static org.nervos.ckb.utils.MoleculeConverter.packBytes;

public class Script {
  public byte[] codeHash;
  public byte[] args;
  public HashType hashType;

  public Script() {
  }

  public Script(byte[] codeHash, byte[] args) {
    this(codeHash, args, HashType.DATA);
  }

  public Script(byte[] codeHash, byte[] args, HashType hashType) {
    this.codeHash = codeHash;
    this.args = args;
    this.hashType = hashType;
  }

  public byte[] computeHash() {
    Blake2b blake2b = new Blake2b();
    blake2b.update(this.pack().toByteArray());
    return blake2b.doFinalBytes();
  }

  public long occupiedCapacity() {
    int byteSize = 1;
    if (codeHash != null) {
      byteSize += codeHash.length;
    }
    if (args != null) {
      byteSize += args.length;
    }
    return Utils.ckbToShannon(byteSize);
  }

  public org.nervos.ckb.type.concrete.Script pack() {
    return org.nervos.ckb.type.concrete.Script.builder()
        .setCodeHash(packByte32(codeHash))
        .setArgs(packBytes(args))
        .setHashType(hashType.pack())
        .build();
  }

  public enum HashType {
    @SerializedName("data")
    DATA(0x00),
    @SerializedName("type")
    TYPE(0x01),
    @SerializedName("data1")
    DATA1(0x02);

    private byte byteValue;

    HashType(int byteValue) {
      this.byteValue = (byte) byteValue;
    }

    public byte pack() {
      return byteValue;
    }

    public static HashType unpack(byte value) {
      switch (value) {
        case 0x00:
          return DATA;
        case 0x01:
          return TYPE;
        case 0x02:
          return DATA1;
        default:
          throw new NullPointerException();
      }
    }
  }
}
