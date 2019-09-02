package org.nervos.ckb.type;

import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Bytes implements Type<byte[]> {

  private byte[] value;

  public Bytes(byte[] value) {
    this.value = value;
  }

  public Bytes(String value) {
    this.value = Numeric.hexStringToByteArray(value);
  }

  public static Bytes fromBytes(byte[] bytes) {
    byte[] dest = new byte[bytes.length - UInt.BYTE_SIZE];
    System.arraycopy(bytes, UInt.BYTE_SIZE, dest, 0, dest.length);
    return new Bytes(dest);
  }

  @Override
  public byte[] toBytes() {
    byte[] lens = new UInt(value.length).toBytes();
    byte[] dest = new byte[UInt.BYTE_SIZE + value.length];
    System.arraycopy(lens, 0, dest, 0, UInt.BYTE_SIZE);
    System.arraycopy(value, 0, dest, UInt.BYTE_SIZE, value.length);
    return dest;
  }

  @Override
  public byte[] getValue() {
    return value;
  }

  @Override
  public int getLength() {
    return UInt.BYTE_SIZE + value.length;
  }
}
