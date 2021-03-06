package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Convert;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CovertTest {

  private Transaction tx;

  @BeforeAll
  void init() {
    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            "100000000000",
            new Script(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                "0xe2193df51d78411601796b35b17b4f8f2cd85bd0")));
    cellOutputs.add(
        new CellOutput(
            "4900000000000",
            new Script(
                "0xe3b513a2105a5d4f833d1fad3d968b96b4510687234cd909f86b3ac450d8a2b5",
                "0x36c329ed630d6ce750712a477543672adab57f4c")));

    tx =
        new Transaction(
            "0",
            Collections.singletonList(
                new CellDep(
                    new OutPoint(
                        "0xbffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a", "1"),
                    CellDep.DEP_GROUP)),
            Collections.singletonList("0x"),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        "0xa80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b50", "0"),
                    "0")),
            cellOutputs,
            Arrays.asList("0x", "0x"),
            Collections.singletonList("0x"));
  }

  @Test
  public void testParseOutPoint() {
    OutPoint outPoint = Convert.parseOutPoint(tx.inputs.get(0).previousOutput);
    Assertions.assertEquals(outPoint.index, "0x0");
  }

  @Test
  public void testParseTransaction() {
    Transaction transaction = Convert.parseTransaction(tx);
    Assertions.assertEquals(transaction.cellDeps.get(0).outPoint.index, "0x1");
    Assertions.assertEquals(transaction.inputs.get(0).since, "0x0");
    Assertions.assertEquals(transaction.outputs.get(0).capacity, "0x174876e800");
  }
}
