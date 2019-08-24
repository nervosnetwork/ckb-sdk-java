package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.exceptions.InvalidHashTypeException;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Script {

  public static final String DATA = "data";
  public static final String TYPE = "type";

  @JsonProperty("code_hash")
  public String codeHash;

  public List<String> args;

  @JsonProperty("hash_type")
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

  public String scriptHash() throws InvalidHashTypeException {
    Blake2b blake2b = new Blake2b();
    if (codeHash != null) {
      blake2b.update(Numeric.hexStringToByteArray(codeHash));
    }
    switch (hashType) {
      case DATA:
        blake2b.update(Numeric.hexStringToByteArray("0"));
        break;
      case TYPE:
        blake2b.update(Numeric.hexStringToByteArray("1"));
        break;
      default:
        throw new InvalidHashTypeException("Invalid hash type!");
    }
    for (String arg : args) {
      blake2b.update(Numeric.hexStringToByteArray(arg));
    }
    return blake2b.doFinalString();
  }
}
