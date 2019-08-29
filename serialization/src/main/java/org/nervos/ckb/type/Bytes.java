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
    byte[] dest = new byte[bytes.length - Int.HEX_SIZE];
    System.arraycopy(bytes, Int.HEX_SIZE, dest, 0, dest.length);
    return new Bytes(dest);
  }

  @Override
  public byte[] toBytes() {
    byte[] lens = new Int(value.length).toBytes();
    byte[] dest = new byte[Int.HEX_SIZE + value.length];
    System.arraycopy(lens, 0, dest, 0, Int.HEX_SIZE);
    System.arraycopy(value, Int.HEX_SIZE, dest, Int.HEX_SIZE, value.length);
    return dest;
  }

  @Override
  public byte[] getValue() {
    return value;
  }
}
