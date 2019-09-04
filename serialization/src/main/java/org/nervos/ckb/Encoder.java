package org.nervos.ckb;

import org.nervos.ckb.type.Type;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Encoder {

  public static byte[] encode(Type type) {
    return type.toBytes();
  }
}
