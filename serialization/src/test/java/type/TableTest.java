package type;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.Encoder;
import org.nervos.ckb.type.base.Type;
import org.nervos.ckb.type.dynamic.Bytes;
import org.nervos.ckb.type.dynamic.Dynamic;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.Byte1;
import org.nervos.ckb.type.fixed.Byte32;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TableTest {

  @Test
  void toBytesTest() {
    ArrayList<Type> types = new ArrayList<>();
    types.add(new Byte32("68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88"));
    types.add(new Byte1("01"));
    List<Bytes> argList = new ArrayList<>();
    argList.add(new Bytes("3954acece65096bfa81258983ddb83915fc56bd8"));
    types.add(new Dynamic<>(argList));
    Table table = new Table(types);
    byte[] result = Encoder.encode(table);
    Assertions.assertEquals(
        "5100000010000000300000003100000068d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88012000000008000000140000003954acece65096bfa81258983ddb83915fc56bd8",
        Numeric.toHexStringNoPrefix(result));
  }

  @Test
  void getLengthTest() {
    ArrayList<Type> types = new ArrayList<>();
    types.add(new Byte32("68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88"));
    types.add(new Byte1("01"));
    List<Bytes> argList = new ArrayList<>();
    argList.add(new Bytes("3954acece65096bfa81258983ddb83915fc56bd8"));
    types.add(new Dynamic<>(argList));
    Table table = new Table(types);
    byte[] result = Encoder.encode(table);
    Assertions.assertEquals(81, result.length);
  }
}
