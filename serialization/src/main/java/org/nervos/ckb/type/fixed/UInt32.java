package org.nervos.ckb.type.fixed;

import org.nervos.ckb.type.base.FixedType;
import org.nervos.ckb.utils.Numeric;

public class UInt32 extends FixedType<Long> {

  public static final int BYTE_SIZE = 4;

  private Long value;

  public UInt32(Long value) {
    this.value = value;
  }

  public UInt32(int value) {
    this.value = (long) value;
  }

  public UInt32(String hexValue) {
    this.value = Numeric.toBigInt(hexValue).longValue();
  }

  // generate int value from little endian bytes
  UInt32(byte[] bytes) {
    long result = 0;
    for (int i = 3; i >= 0; i--) {
      result += ((long) bytes[i] & 0xff) << 8 * i;
    }
    this.value = result;
  }

  /*
   * @return little endian bytes
   * */
  @Override
  public byte[] toBytes() {
    byte[] result = new byte[4];
    result[3] = (byte) (value >> 24 & 0xff);
    result[2] = (byte) (value >> 16 & 0xff);
    result[1] = (byte) (value >> 8 & 0xff);
    result[0] = (byte) (value & 0xff);
    return result;
  }

  @Override
  public Long getValue() {
    return value;
  }

  @Override
  public int getLength() {
    return BYTE_SIZE;
  }
}
