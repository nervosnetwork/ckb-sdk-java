package org.nervos.ckb.transaction;

import java.util.ArrayList;
import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class NumberUtils {

  public static List<Integer> regionToList(int start, int length) {
    List<Integer> integers = new ArrayList<>();
    for (int i = start; i < (start + length); i++) {
      integers.add(i);
    }
    return integers;
  }

  public static String getZeros(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append("0");
    }
    return sb.toString();
  }
}
