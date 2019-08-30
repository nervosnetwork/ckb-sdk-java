import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.Encoder;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.type.Table;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class EncoderTest {

  @Test
  void toTableTest() {
    Script script =
        new Script(
            "68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
            Collections.singletonList("3954acece65096bfa81258983ddb83915fc56bd8"),
            "type");
    Object table = Encoder.toTable(script);
    Assertions.assertNotNull(table);
  }

  @Test
  void encodeTest() {
    Script script =
        new Script(
            "68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
            Collections.singletonList("3954acece65096bfa81258983ddb83915fc56bd8"),
            "type");
    Table table = Encoder.toTable(script);
    byte[] expected =
        Numeric.hexStringToByteArray(
            "5100000010000000300000003100000068d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88012000000008000000140000003954acece65096bfa81258983ddb83915fc56bd8");
    Assertions.assertArrayEquals(expected, Encoder.encode(table));
  }
}
