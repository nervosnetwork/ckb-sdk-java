package type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.dynamic.Bytes;
import org.nervos.ckb.utils.Numeric;

public class BytesTest {

  @Test
  public void toBytesTest() {
    Bytes bytes = new Bytes("3954acece65096bfa81258983ddb83915fc56bd8");
    byte[] expected =
        Numeric.hexStringToByteArray("140000003954acece65096bfa81258983ddb83915fc56bd8");
    Assertions.assertArrayEquals(expected, bytes.toBytes());
  }

  @Test
  public void getLengthTest() {
    Bytes bytes = new Bytes("3954acece65096bfa81258983ddb83915fc56bd8");
    Assertions.assertEquals(24, bytes.getLength());
  }
}
