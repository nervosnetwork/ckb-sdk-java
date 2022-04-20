package org.nervos.ckb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.indexer.NumberUtils;

import java.util.Arrays;

public class NumberUtilsTest {

  @Test
  public void testRegionToList() {
    Assertions.assertEquals(NumberUtils.regionToList(3, 5), Arrays.asList(3, 4, 5, 6, 7));
  }
}
