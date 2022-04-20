package type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.utils.Numeric;

import static utils.TestUtils.createScript;

public class CellOutputTest {

  @Test
  public void testOccupiedCapacity() {
    CellOutput cellOutput =
        new CellOutput(
            100000000000L,
            createScript(
                "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                Script.HashType.TYPE));
    Assertions.assertEquals(6100000000L, cellOutput.occupiedCapacity(new byte[]{}));
  }

  @Test
  public void testOccupiedCapacityWithData() {
    CellOutput cellOutput =
        new CellOutput(
            100000000000L,
            createScript(
                "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                Script.HashType.TYPE));
    Assertions.assertEquals(
        9300000000L,
        cellOutput.occupiedCapacity(Numeric.hexStringToByteArray("0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88")));
  }
}
