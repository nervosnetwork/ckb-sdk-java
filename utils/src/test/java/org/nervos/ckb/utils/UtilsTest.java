package org.nervos.ckb.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  public void testCkbToShannon() {
    Assertions.assertEquals(234300000000L, Utils.ckbToShannon(2343));
  }

  @Test
  public void testCkbToShannonWithDouble() {
    Assertions.assertEquals(2560000, Utils.ckbToShannon(0.0256));
  }
}
