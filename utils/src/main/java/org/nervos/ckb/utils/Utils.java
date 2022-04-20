package org.nervos.ckb.utils;

public class Utils {

  public static long ckbToShannon(long value) {
    return (long) (value * Math.pow(10, 8));
  }

  public static long ckbToShannon(double value) {
    return (long) (value * Math.pow(10, 8));
  }
}
