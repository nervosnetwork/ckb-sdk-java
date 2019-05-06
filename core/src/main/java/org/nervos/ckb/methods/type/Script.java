package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.utils.Numeric;

/** Created by duanyytop on 2019-01-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Script {
  public static final String ALWAYS_SUCCESS_HASH =
      "0000000000000000000000000000000000000000000000000000000000000001";

  @JsonProperty("code_hash")
  public String codeHash;

  public List<String> args;

  public Script() {}

  public Script(String codeHash, List<String> args) {
    this.codeHash = codeHash;
    this.args = args;
  }

  public static Script alwaysSuccess() {
    return new Script(ALWAYS_SUCCESS_HASH, Collections.emptyList());
  }

  public String getTypeHash() {
    Blake2b blake2b = new Blake2b();
    if (codeHash != null) {
      blake2b.update(Numeric.hexStringToByteArray(codeHash));
    }
    for (String arg : args) {
      blake2b.update(Numeric.hexStringToByteArray(arg));
    }
    return blake2b.doFinalString();
  }
}
