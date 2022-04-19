package org.nervos.ckb.utils;

import java.math.BigInteger;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Utils {

  public static long ckbToShannon(long value) {
    return (long) (value * Math.pow(10, 8));
  }

  public static long ckbToShannon(double value) {
    return (long) (value * Math.pow(10, 8));
  }
}
