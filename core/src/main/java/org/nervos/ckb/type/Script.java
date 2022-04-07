package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.nervos.ckb.Encoder;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.utils.Serializer;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Script {
  @SerializedName("code_hash")
  public byte[] codeHash;

  public byte[] args;

  @SerializedName("hash_type")
  public HashType hashType;

  public Script() {}

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
    blake2b.update(Encoder.encode(Serializer.serializeScript(this)));
    return blake2b.doFinalBytes();
  }

  public BigInteger occupiedCapacity() {
    int byteSize = 1;
    if (codeHash != null) {
      byteSize += codeHash.length;
    }
    if (args != null) {
      byteSize += args.length;
    }
    return Utils.ckbToShannon(byteSize);
  }

  public enum HashType {
    @SerializedName("data")
    DATA,
    @SerializedName("type")
    TYPE,
    @SerializedName("data1")
    DATA1;
  }
}
