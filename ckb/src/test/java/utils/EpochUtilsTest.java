package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.EpochUtils;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class EpochUtilsTest {

  @Test
  void parseTest() {
    EpochUtils.EpochInfo params = EpochUtils.parse("0x70800fd000058");
    Assertions.assertEquals(253, params.index);
    Assertions.assertEquals(88, params.number);
    Assertions.assertEquals(1800, params.length);
  }

  @Test
  void generateTest() {
    String epoch = EpochUtils.generate(1800, 253, 88);
    Assertions.assertEquals("0x70800fd000058", epoch);
  }

  @Test
  void parseSinceTest() {
    String epoch = EpochUtils.generateSince(1800, 253, 88);
    Assertions.assertEquals("0x20070800fd000058", epoch);
  }
}
