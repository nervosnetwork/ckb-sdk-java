package type;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.fixed.*;
import org.nervos.ckb.utils.Numeric;

/**
 * Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class FixedTest {

  @Test
  public void toBytesTest() {
    Byte32 txHash =
        new Byte32("0x0000000000000000000000000000000000000000000000000000000000000000");
    UInt32 index = new UInt32("4294967295");
    UInt64 sinceUInt64 = new UInt64(1L);
    Struct inputs = new Struct(sinceUInt64, new Struct(txHash, index));
    Fixed<Struct> structFixed = new Fixed<>(Collections.singletonList(inputs));
    Assertions.assertArrayEquals(
        Numeric.hexStringToByteArray(
            "0x010000000100000000000000000000000000000000000000000000000000000000000000000000000000000095729694"),
        structFixed.toBytes());
  }
}
