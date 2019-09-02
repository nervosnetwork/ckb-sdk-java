package type;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Bytes;
import org.nervos.ckb.type.DynVec;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class DynVecTest {

  @Test
  void toBytesTest() {
    List<String> args = new ArrayList<>();
    args.add("68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88");
    args.add("3954acece65096bfa81258983ddb83915fc56bd8");
    List<Bytes> argList = new ArrayList<>();
    for (String arg : args) {
      argList.add(new Bytes(arg));
    }
    DynVec dynVec = new DynVec(argList);
    byte[] expected =
        Numeric.hexStringToByteArray(
            "0x480000000c000000300000002000000068d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88140000003954acece65096bfa81258983ddb83915fc56bd8");
    Assertions.assertArrayEquals(expected, dynVec.toBytes());
  }
}
