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

  public static long generateSince(long length, long index, long number) {
    // flag || epoch
    long since = 0x20L << 56;
    // epoch representation
    since = since | (length << 40 | index << 24 | number);
    return since;
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
