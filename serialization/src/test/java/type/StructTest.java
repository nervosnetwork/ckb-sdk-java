package type;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.base.Type;
import org.nervos.ckb.type.fixed.Byte1;
import org.nervos.ckb.type.fixed.Byte32;
import org.nervos.ckb.type.fixed.Struct;
import org.nervos.ckb.utils.Numeric;

/**
 * Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class StructTest {

  @Test
  void toBytesTest() {
    Byte1 byte1 = new Byte1("ab");
    Byte32 byte32 = new Byte32("0102030405060708090001020304050607080900010203040506070809000102");

    List<Type> args = new ArrayList<>();
    args.add(byte1);
    args.add(byte32);

    Struct struct = new Struct(args);
    Assertions.assertArrayEquals(
        Numeric.hexStringToByteArray(
            "0xab0102030405060708090001020304050607080900010203040506070809000102"),
        struct.toBytes());
  }
}
