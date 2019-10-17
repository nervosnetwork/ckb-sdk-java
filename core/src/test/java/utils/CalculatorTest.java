package utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Calculator;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CalculatorTest {

  @Test
  void testCalculateTransactionSize() {
    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            "0x174876e800",
            new Script(
                "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                Script.TYPE),
            new Script(
                "0xece45e0979030e2f8909f76258631c42333b1e906fd9701ec3600a464a90b8f6",
                "0x",
                Script.DATA)));
    cellOutputs.add(
        new CellOutput(
            "0x59e1416a5000",
            new Script(
                "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                Script.TYPE)));

    Transaction tx =
        new Transaction(
            "0x0",
            Arrays.asList(
                new CellDep(
                    new OutPoint(
                        "0xc12386705b5cbb312b693874f3edf45c43a274482e27b8df0fd80c8d3f5feb8b",
                        "0x0"),
                    CellDep.DEP_GROUP),
                new CellDep(
                    new OutPoint(
                        "0x0fb4945d52baf91e0dee2a686cdd9d84cad95b566a1d7409b970ee0a0f364f60",
                        "0x2"),
                    CellDep.CODE)),
            Collections.emptyList(),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65",
                        "0x1"),
                    "0x0")),
            cellOutputs,
            Arrays.asList("0x1234", "0x"),
            Collections.singletonList(
                "0x82df73581bcd08cb9aa270128d15e79996229ce8ea9e4f985b49fbf36762c5c37936caf3ea3784ee326f60b8992924fcf496f9503c907982525a3436f01ab32900"));

    Assertions.assertEquals(Calculator.calculateSerializedSizeInBlock(tx), 536);
  }

  @Test
  public void testCalculateMinTransactionFee() {
    Assertions.assertEquals(
        Calculator.calculateMinTransactionFee(BigInteger.valueOf(1035), BigInteger.valueOf(900)),
        BigInteger.valueOf(932));
    Assertions.assertEquals(
        Calculator.calculateMinTransactionFee(BigInteger.valueOf(900), BigInteger.valueOf(900)),
        BigInteger.valueOf(810));
  }
}
