package type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class UInt64Test {

  @Test
  void toBytesTest() {
    UInt64 data = new UInt64(25689834934789L);
    Assertions.assertEquals("05527c615d170000", Numeric.toHexStringNoPrefix(data.toBytes()));
  }

  @Test
  void toFromBytesTest() {
    UInt64 data = new UInt64(Numeric.hexStringToByteArray("0x9abd020000000000"));
    Assertions.assertEquals("179610", data.getValue().toString());
  }

  @Test
  void getLengthTest() {
    UInt64 data = new UInt64(25689834934789L);
    Assertions.assertEquals(8, data.toBytes().length);
  }
}
