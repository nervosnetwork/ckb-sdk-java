package org.nervos.ckb.type;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Byte8 implements Type<byte[]> {

  private byte[] value;

  public Byte8(byte[] value) {
    if (value.length != 2) {
      throw new UnsupportedOperationException("Byte32 length error");
    }
    this.value = value;
  }

  @Override
  public byte[] toBytes() {
    return value;
  }

  @Override
  public byte[] getValue() {
    return value;
  }
}
