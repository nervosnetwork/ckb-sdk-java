package org.nervos.ckb.type;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Int implements Type<Integer> {

  public static final int HEX_SIZE = 8;

  private Integer value;

  Int(Integer value) {
    this.value = value;
  }

  // generate int value from little endian bytes
  Int(byte[] bytes) {
    int result = 0;
    for (int i = 3; i >= 0; i--) {
      result += ((int) bytes[i] & 0xff) << 8 * i;
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
  public Integer getValue() {
    return value;
  }
}
