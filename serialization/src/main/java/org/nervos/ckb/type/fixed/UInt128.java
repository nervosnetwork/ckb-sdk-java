package org.nervos.ckb.type.fixed;

import java.math.BigInteger;
import org.nervos.ckb.type.base.FixedType;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class UInt128 extends FixedType<BigInteger> {

  private BigInteger value;

  public UInt128(BigInteger value) {
    this.value = value;
  }

  public UInt128(Long value) {
    this.value = BigInteger.valueOf(value);
  }

  public UInt128(String hexString) {
    this(Numeric.hexStringToByteArray(hexString));
  }

  // generate int value from little endian bytes
  public UInt128(byte[] bytes) {
    BigInteger result = BigInteger.ZERO;
    for (int i = 15; i >= 0; i--) {
      result = result.add(BigInteger.valueOf((long) bytes[i] & 0xff).shiftLeft(8 * i));
    }
    this.value = result;
  }

  /*
   * @return little endian bytes
   * */
  @Override
  public byte[] toBytes() {
    byte[] result = new byte[16];
    result[15] = (byte) (value.shiftRight(120).longValue() & 0xff);
    result[14] = (byte) (value.shiftRight(112).longValue() & 0xff);
    result[13] = (byte) (value.shiftRight(104).longValue() & 0xff);
    result[12] = (byte) (value.shiftRight(96).longValue() & 0xff);
    result[11] = (byte) (value.shiftRight(88).longValue() & 0xff);
    result[10] = (byte) (value.shiftRight(80).longValue() & 0xff);
    result[9] = (byte) (value.shiftRight(72).longValue() & 0xff);
    result[8] = (byte) (value.shiftRight(64).longValue() & 0xff);
    result[7] = (byte) (value.shiftRight(56).longValue() & 0xff);
    result[6] = (byte) (value.shiftRight(48).longValue() & 0xff);
    result[5] = (byte) (value.shiftRight(40).longValue() & 0xff);
    result[4] = (byte) (value.shiftRight(32).longValue() & 0xff);
    result[3] = (byte) (value.shiftRight(24).longValue() & 0xff);
    result[2] = (byte) (value.shiftRight(16).longValue() & 0xff);
    result[1] = (byte) (value.shiftRight(8).longValue() & 0xff);
    result[0] = (byte) (value.longValue() & 0xff);
    return result;
  }

  @Override
  public BigInteger getValue() {
    return value;
  }

  @Override
  public int getLength() {
    return 16;
  }
}
