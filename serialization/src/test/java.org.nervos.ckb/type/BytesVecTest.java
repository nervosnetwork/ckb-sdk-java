package type;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Bytes;
import org.nervos.ckb.type.BytesVec;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BytesVecTest {

  @Test
  void toBytesTest() {
    List<String> args = new ArrayList<>();
    args.add("68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88");
    args.add("3954acece65096bfa81258983ddb83915fc56bd8");
    List<Bytes> argList = new ArrayList<>();
    for (String arg : args) {
      argList.add(new Bytes(arg));
    }
    BytesVec bytesVec = new BytesVec(argList);
    System.out.println(Numeric.toHexString(bytesVec.toBytes()));
    byte[] bytes = {0x01};
    Assertions.assertArrayEquals(bytes, bytesVec.toBytes());
  }
}
