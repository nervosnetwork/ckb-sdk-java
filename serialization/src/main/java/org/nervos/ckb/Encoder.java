package org.nervos.ckb;

import org.nervos.ckb.type.base.Type;

public class Encoder {

  public static byte[] encode(Type type) {
    return type.toBytes();
  }
}
