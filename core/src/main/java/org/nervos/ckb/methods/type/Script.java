package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.type.*;

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

  public String scriptHash() {
    ArrayList<Type> types = new ArrayList<>();
    types.add(new Byte32(this.codeHash));
    types.add(new Byte1(Script.DATA.equals(this.hashType) ? "00" : "0x01"));
    List<Bytes> argList = new ArrayList<>();
    for (String arg : this.args) {
      argList.add(new Bytes(arg));
    }
    types.add(new DynVec(argList));
    byte[] script = new Table(types).toBytes();
    Blake2b blake2b = new Blake2b();
    blake2b.update(script);
    return blake2b.doFinalString();
  }
}
