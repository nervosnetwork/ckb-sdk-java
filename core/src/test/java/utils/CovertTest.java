package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Convert;
import org.nervos.ckb.utils.Numeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static utils.TestUtils.createScript;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CovertTest {

  private Transaction tx;

  @BeforeAll
  void init() {
    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            100000000000L,
            createScript(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                "0xe2193df51d78411601796b35b17b4f8f2cd85bd0")));
    cellOutputs.add(
        new CellOutput(
            4900000000000L,
            createScript(
                "0xe3b513a2105a5d4f833d1fad3d968b96b4510687234cd909f86b3ac450d8a2b5",
                "0x36c329ed630d6ce750712a477543672adab57f4c")));

    tx =
        new Transaction(
            0,
            Collections.singletonList(
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0xbffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a"),
                        1),
                    CellDep.DepType.DEP_GROUP)),
            Collections.singletonList(new byte[]{}),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0xa80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b50"),
                        0))),
            cellOutputs,
            Arrays.asList(new byte[]{}, new byte[]{}),
            Collections.singletonList(new byte[]{}));
  }

  @Test
  public void testParseOutPoint() {
    OutPoint outPoint = Convert.parseOutPoint(tx.inputs.get(0).previousOutput);
    Assertions.assertEquals(outPoint.index, 0);
  }

  @Test
  public void testParseTransaction() {
    Transaction transaction = Convert.parseTransaction(tx);
    Assertions.assertEquals(transaction.cellDeps.get(0).outPoint.index, 1);
    Assertions.assertEquals(transaction.inputs.get(0).since, 0);
    Assertions.assertEquals(transaction.outputs.get(0).capacity, 0x174876e800L);
  }
}
