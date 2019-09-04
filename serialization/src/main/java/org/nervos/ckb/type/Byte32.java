package org.nervos.ckb.type;

import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Byte32 implements Type<byte[]> {

  private byte[] value;

  public Byte32(byte[] value) {
    if (value.length != 32) {
      throw new UnsupportedOperationException("Byte32 length error");
    }
    this.value = value;
  }

  public Byte32(String value) {
    byte[] bytes = Numeric.hexStringToByteArray(value);
    if (bytes.length != 32) {
      throw new UnsupportedOperationException("Byte32 length error");
    }
    this.value = bytes;
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
    return 32;
  }
}
