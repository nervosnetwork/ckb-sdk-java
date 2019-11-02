package org.nervos.ckb;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.NumberUtils;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class NumberUtilsTest {

  @Test
  public void testRegionToList() {
    Assertions.assertEquals(NumberUtils.regionToList(3, 5), Arrays.asList(3, 4, 5, 6, 7));
  }
}
