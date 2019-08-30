package type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Byte1;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Byte1Test {

  @Test
  public void toByte1InitEmptyTest() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> {
          byte[] bytes = {};
          Byte1 byte1 = new Byte1(bytes);
          byte1.toBytes();
        });
  }

  @Test
  public void toByte1InitTest() {
    Assertions.assertThrows(
        UnsupportedOperationException.class,
        () -> {
          byte[] bytes = {0x01, 0x02};
          Byte1 byte1 = new Byte1(bytes);
          byte1.toBytes();
        });
  }

  @Test
  public void toByte1Test() {
    Byte1 byte1 = new Byte1("1");
    Byte1 byte2 = new Byte1("01");
    byte[] expected = {0x01};
    Assertions.assertArrayEquals(expected, byte1.toBytes());
    Assertions.assertArrayEquals(expected, byte2.toBytes());
  }
}
