package org.nervos.ckb.type.fixed;

import org.nervos.ckb.type.base.FixedType;
import org.nervos.ckb.utils.Numeric;

import java.math.BigInteger;

public class UInt64 extends FixedType<BigInteger> {

  private BigInteger value;

  public UInt64(BigInteger value) {
    this.value = value;
  }

  public UInt64(Long value) {
    this.value = BigInteger.valueOf(value);
  }

  public UInt64(int value) {
    this.value = BigInteger.valueOf(value);
  }

  public UInt64(String value) {
    this.value = Numeric.toBigInt(value);
  }

  // generate int value from little endian bytes
  public UInt64(byte[] bytes) {
    long result = 0;
    for (int i = bytes.length - 1; i >= 0; i--) {
      result += ((long) bytes[i] & 0xff) << 8 * i;
    }
    this.value = BigInteger.valueOf(result);
  }

  /*
   * @return little endian bytes
   * */
  @Override
  public byte[] toBytes() {
    byte[] result = new byte[8];
    long valueLong = value.longValue();
    result[7] = (byte) (valueLong >> 56 & 0xff);
    result[6] = (byte) (valueLong >> 48 & 0xff);
    result[5] = (byte) (valueLong >> 40 & 0xff);
    result[4] = (byte) (valueLong >> 32 & 0xff);
    result[3] = (byte) (valueLong >> 24 & 0xff);
    result[2] = (byte) (valueLong >> 16 & 0xff);
    result[1] = (byte) (valueLong >> 8 & 0xff);
    result[0] = (byte) (valueLong & 0xff);
    return result;
  }

  @Override
  public BigInteger getValue() {
    return value;
  }

  @Override
  public int getLength() {
    return 8;
  }
}
