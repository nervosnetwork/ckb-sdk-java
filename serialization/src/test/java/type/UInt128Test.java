package type;

import java.math.BigInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.fixed.UInt128;
import org.nervos.ckb.utils.Numeric;

/**
 * Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class UInt128Test {

  @Test
  void toBytesTest() {
    UInt128 data = new UInt128(BigInteger.TEN.pow(30));
    Assertions.assertEquals(
        "00000040eaed7446d09c2c9f0c000000", Numeric.toHexStringNoPrefix(data.toBytes()));
  }

  @Test
  void toFromBytesTest() {
    UInt128 data = new UInt128(Numeric.hexStringToByteArray("00000040eaed7446d09c2c9f0c000000"));
    Assertions.assertEquals(BigInteger.TEN.pow(30), data.getValue());
  }

  @Test
  void toBytesTest2() {
    UInt128 data = new UInt128(10000000000L);
    Assertions.assertEquals(
        "00e40b54020000000000000000000000", Numeric.toHexStringNoPrefix(data.toBytes()));
  }

  @Test
  void toFromBytesTest2() {
    UInt128 data = new UInt128(Numeric.hexStringToByteArray("00e40b54020000000000000000000000"));
    Assertions.assertEquals(10000000000L, data.getValue().longValue());
  }

  @Test
  void getLengthTest() {
    UInt128 data = new UInt128(BigInteger.TEN.pow(30));
    Assertions.assertEquals(16, data.toBytes().length);
  }
}
