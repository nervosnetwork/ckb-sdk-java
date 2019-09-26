package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.nervos.ckb.Encoder;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Script {

  public static final String DATA = "data";
  public static final String TYPE = "type";

  @SerializedName("code_hash")
  public String codeHash;

  public List<String> args;

  @SerializedName("hash_type")
  public String hashType;

  public Script() {}

  public Script(String codeHash, List<String> args) {
    this.codeHash = codeHash;
    this.args = args;
    this.hashType = DATA;
  }

  public Script(String codeHash, List<String> args, String hashType) {
    this.codeHash = codeHash;
    this.args = args;
    this.hashType = hashType;
  }

  public String computeHash() {
    Blake2b blake2b = new Blake2b();
    blake2b.update(Encoder.encode(Serializer.serializeScript(this)));
    return blake2b.doFinalString();
  }
}
