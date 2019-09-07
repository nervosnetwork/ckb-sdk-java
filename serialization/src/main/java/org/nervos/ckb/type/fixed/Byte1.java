package org.nervos.ckb.type.fixed;

import org.nervos.ckb.type.base.FixedType;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Byte1 extends FixedType<byte[]> {

  private byte[] value;

  public Byte1(byte[] value) {
    if (value.length != 1) {
      throw new UnsupportedOperationException("Byte1 length error");
    }
    this.value = value;
  }

  public Byte1(String value) {
    this.value = Numeric.hexStringToByteArray(value);
  }

  @Override
  public byte[] toBytes() {
    return value;
  }

  @Override
  public byte[] getValue() {
    return value;
  }

  @Override
  public int getLength() {
    return 1;
  }
}
