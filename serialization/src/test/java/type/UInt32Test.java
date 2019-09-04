package type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class UInt64Test {

  @Test
  void toBytesTest() {
    UInt64 data = new UInt64(256L);
    System.out.println(Numeric.toHexStringNoPrefix(data.toBytes()));
    Assertions.assertEquals(Numeric.toHexStringNoPrefix(data.toBytes()), "0001000000000000");
  }

  @Test
  void getLengthTest() {
    UInt64 data = new UInt64(256L);
    Assertions.assertEquals(data.toBytes().length, 8);
  }
}
