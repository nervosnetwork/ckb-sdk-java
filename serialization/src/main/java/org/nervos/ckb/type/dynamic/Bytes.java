package org.nervos.ckb.type.dynamic;

import org.nervos.ckb.type.base.DynType;
import org.nervos.ckb.type.fixed.UInt32;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Bytes extends DynType<byte[]> {

  private byte[] value;

  public Bytes(byte[] value) {
    this.value = value;
  }

  public Bytes(String value) {
    this.value = Numeric.hexStringToByteArray(value);
  }

  public static Bytes fromBytes(byte[] bytes) {
    byte[] dest = new byte[bytes.length - UInt32.BYTE_SIZE];
    System.arraycopy(bytes, UInt32.BYTE_SIZE, dest, 0, dest.length);
    return new Bytes(dest);
  }

  @Override
  public byte[] toBytes() {
    byte[] dest = new byte[UInt32.BYTE_SIZE + value.length];
    System.arraycopy(new UInt32(value.length).toBytes(), 0, dest, 0, UInt32.BYTE_SIZE);
    System.arraycopy(value, 0, dest, UInt32.BYTE_SIZE, value.length);
    return dest;
  }

  @Override
  public byte[] getValue() {
    return value;
  }

  @Override
  public int getLength() {
    return UInt32.BYTE_SIZE + value.length;
  }
}
