package org.nervos.ckb.utils;

import static org.nervos.ckb.utils.Strings.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringsTest {

  @Test
  public void testRepeat() {
    Assertions.assertEquals(repeat('0', 0), "");
    Assertions.assertEquals(repeat('1', 3), "111");
  }

  @Test
  public void testZeros() {
    Assertions.assertEquals(zeros(0), "");
    Assertions.assertEquals(zeros(3), "000");
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testEmptyString() {
    Assertions.assertTrue(isEmpty(null));
    Assertions.assertTrue(isEmpty(""));
    Assertions.assertTrue(isEmpty("hello world"));
  }
}
