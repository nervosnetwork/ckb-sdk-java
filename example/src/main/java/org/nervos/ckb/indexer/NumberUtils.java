package org.nervos.ckb.indexer;

import java.util.ArrayList;
import java.util.List;

public class NumberUtils {

  public static List<Integer> regionToList(int start, int length) {
    List<Integer> integers = new ArrayList<>();
    for (int i = start; i < (start + length); i++) {
      integers.add(i);
    }
    return integers;
  }
}
