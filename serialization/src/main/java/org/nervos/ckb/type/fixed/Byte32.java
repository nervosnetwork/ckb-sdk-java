package org.nervos.ckb.type.fixed;

import org.nervos.ckb.type.base.FixedType;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Byte32 extends FixedType<byte[]> {

  private byte[] value;

  public Byte32(byte[] value) {
    if (value.length != 32) {
      throw new UnsupportedOperationException("Byte32 length error");
    }
    this.value = value;
  }

  public Byte32(String value) {
    byte[] bytes = Numeric.hexStringToByteArray(value);
    if (bytes.length > 32) {
      throw new UnsupportedOperationException("Byte32 length error");
    } else if (bytes.length < 32) {
      byte[] dest =
          new byte[] {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00
          };
      System.arraycopy(bytes, 0, dest, 0, bytes.length);
      this.value = dest;
    } else {
      this.value = bytes;
    }
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
