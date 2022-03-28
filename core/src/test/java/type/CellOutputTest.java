package type;

import java.math.BigInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellOutput;

import static utils.TestUtils.createScript;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutputTest {

  @Test
  public void testOccupiedCapacity() {
    CellOutput cellOutput =
        new CellOutput(
            "100000000000",
            createScript(
                "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                Script.HashType.TYPE));
    Assertions.assertEquals(BigInteger.valueOf(6100000000L), cellOutput.occupiedCapacity("0x"));
  }

  @Test
  public void testOccupiedCapacityWithData() {
    CellOutput cellOutput =
        new CellOutput(
            "100000000000",
            createScript(
                "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                Script.HashType.TYPE));
    Assertions.assertEquals(
        BigInteger.valueOf(9300000000L),
        cellOutput.occupiedCapacity(
            "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88"));
  }
}
