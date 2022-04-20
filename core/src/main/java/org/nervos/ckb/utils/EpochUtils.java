package org.nervos.ckb.utils;

import java.math.BigInteger;

public class EpochUtils {

  public static EpochInfo parse(byte[] epoch) {
    long epochLong = Numeric.toBigInt(epoch).longValue();
    long length = (epochLong >> 40) & 0xFFFF;
    long index = (epochLong >> 24) & 0xFFFF;
    long number = epochLong & 0xFFFFFF;
    return new EpochInfo(length, index, number);
  }

  public static EpochInfo parse(long epoch) {
    return parse(BigInteger.valueOf(epoch).toByteArray());
  }

  public static String generate(long length, long index, long number) {
    BigInteger epoch = BigInteger.valueOf((length << 40) + (index << 24) + number);
    return Numeric.toHexStringWithPrefix(epoch);
  }

  public static String generateSince(long length, long index, long number) {
    BigInteger bigInteger = Numeric.toBigInt("0x20").shiftLeft(56);
    BigInteger epoch = BigInteger.valueOf((length << 40) + (index << 24) + number);
    return Numeric.toHexStringWithPrefix(bigInteger.add(epoch));
  }

  public static class EpochInfo {
    public long length;
    public long index;
    public long number;

    public EpochInfo(long length, long index, long number) {
      this.length = length;
      this.index = index;
      this.number = number;
    }
  }
}
