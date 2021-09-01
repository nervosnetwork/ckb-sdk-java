package org.nervos.ckb.utils;

import java.math.BigInteger;

public class AmountUtils {
  public static BigInteger ckbToShannon(BigInteger value) {
    return Utils.ckbToShannon(value);
  }

  public static BigInteger ckbToShannon(long value) {
    return Utils.ckbToShannon(value);
  }

  public static BigInteger ckbToShannon(double value) {
    return Utils.ckbToShannon(value);
  }
}
