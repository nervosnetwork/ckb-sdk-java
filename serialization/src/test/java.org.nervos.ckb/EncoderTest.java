import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.Encoder;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.type.Table;
import org.nervos.ckb.utils.Numeric;

import java.util.Collections;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class SerializationTest {

  @Test
  void scriptEncodeTest() {
    Script script =
        new Script(
            "68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
            Collections.singletonList("3954acece65096bfa81258983ddb83915fc56bd8"),
            "type");
    Table table = Encoder.toTable(script);
    byte[] result = Encoder.encode(table);
    Assertions.assertEquals(
        "5100000010000000300000003100000068d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88012000000008000000140000003954acece65096bfa81258983ddb83915fc56bd8",
        Numeric.toHexStringNoPrefix(result));
  }
}
