package type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.fixed.Byte32;
import org.nervos.ckb.utils.Numeric;

/**
 * Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class Byte32Test {

  @Test
  public void byte32InitEmptyErrorTest() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> {
          byte[] bytes = {};
          Byte32 byte32 = new Byte32(bytes);
          byte32.toBytes();
        });
  }

  @Test
  public void byte32InitErrorTest() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> {
          byte[] bytes = {0x01, 0x02, 0x02, 0x03};
          Byte32 byte32 = new Byte32(bytes);
          byte32.toBytes();
        });
  }

  @Test
  public void byte32InitTest() {
    Assertions.assertDoesNotThrow(
        () -> {
          byte[] bytes = {
              0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
              0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
              0x01, 0x01, 0x01, 0x01,
          };
          Byte32 byte32 = new Byte32(bytes);
          byte32.toBytes();
        });
  }

  @Test
  public void toBytesTest() {
    Byte32 byte32 = new Byte32("68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88");
    byte[] expected =
        Numeric.hexStringToByteArray(
            "68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88");
    Assertions.assertArrayEquals(expected, byte32.toBytes());
  }
}
