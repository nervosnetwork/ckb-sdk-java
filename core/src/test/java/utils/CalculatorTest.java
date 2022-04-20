package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static utils.TestUtils.createScript;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalculatorTest {

  @Test
  void testCalculateTransactionSize() {
    Transaction tx =
        new Transaction(
            0,
            Arrays.asList(
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0xc12386705b5cbb312b693874f3edf45c43a274482e27b8df0fd80c8d3f5feb8b"),
                        0),
                    CellDep.DepType.DEP_GROUP),
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x0fb4945d52baf91e0dee2a686cdd9d84cad95b566a1d7409b970ee0a0f364f60"),
                        2),
                    CellDep.DepType.CODE)),
            Collections.emptyList(),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65"),
                        1))),
            Arrays.asList(
                new CellOutput(
                    Long.valueOf("174876e800", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE),
                    createScript(
                        "0xece45e0979030e2f8909f76258631c42333b1e906fd9701ec3600a464a90b8f6",
                        "0x",
                        Script.HashType.DATA)),
                new CellOutput(
                    Long.valueOf("59e1416a5000", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE))),
            Arrays.asList(Numeric.hexStringToByteArray("0x1234"), new byte[]{}),
            Collections.singletonList(
                Numeric.hexStringToByteArray(
                    "0x82df73581bcd08cb9aa270128d15e79996229ce8ea9e4f985b49fbf36762c5c37936caf3ea3784ee326f60b8992924fcf496f9503c907982525a3436f01ab32900")));
    Assertions.assertEquals(Calculator.calculateTransactionSize(tx), 536);
  }

  @Test
  public void testCalculateTransactionFee() {
    Transaction tx =
        new Transaction(
            0,
            Arrays.asList(
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0xc12386705b5cbb312b693874f3edf45c43a274482e27b8df0fd80c8d3f5feb8b"),
                        0),
                    CellDep.DepType.DEP_GROUP),
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x0fb4945d52baf91e0dee2a686cdd9d84cad95b566a1d7409b970ee0a0f364f60"),
                        2),
                    CellDep.DepType.CODE)),
            Collections.emptyList(),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65"),
                        1))),
            Arrays.asList(
                new CellOutput(
                    Long.valueOf("174876e800", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE),
                    createScript(
                        "0xece45e0979030e2f8909f76258631c42333b1e906fd9701ec3600a464a90b8f6",
                        "0x",
                        Script.HashType.DATA)),
                new CellOutput(
                    Long.valueOf("59e1416a5000", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE))),
            Arrays.asList(Numeric.hexStringToByteArray("0x1234"), new byte[]{}),
            Collections.singletonList(
                Numeric.hexStringToByteArray(
                    "0x82df73581bcd08cb9aa270128d15e79996229ce8ea9e4f985b49fbf36762c5c37936caf3ea3784ee326f60b8992924fcf496f9503c907982525a3436f01ab32900")));
    Assertions.assertEquals(
        Calculator.calculateTransactionFee(tx, 900), BigInteger.valueOf(483));
  }

  @Test
  void testCalculateTransactionSize1V1() {
    Transaction tx =
        new Transaction(
            0,
            Collections.singletonList(
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0xc12386705b5cbb312b693874f3edf45c43a274482e27b8df0fd80c8d3f5feb8b"),
                        0),
                    CellDep.DepType.DEP_GROUP)),
            Collections.emptyList(),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65"),
                        1))),
            Collections.singletonList(
                new CellOutput(
                    Long.valueOf("174876e800", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE))),
            Collections.singletonList(new byte[]{}),
            Collections.singletonList(witnessPlaceholder()));
    Assertions.assertEquals(Calculator.calculateTransactionSize(tx), 355);
  }

  @Test
  void testCalculateTransactionSize1V2() {
    Transaction tx =
        new Transaction(
            0,
            Collections.singletonList(
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0xc12386705b5cbb312b693874f3edf45c43a274482e27b8df0fd80c8d3f5feb8b"),
                        0),
                    CellDep.DepType.DEP_GROUP)),
            Collections.emptyList(),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65"),
                        1))),
            Arrays.asList(
                new CellOutput(
                    Long.valueOf("174876e800", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE)),
                new CellOutput(
                    Long.valueOf("174876e800", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE))),
            Arrays.asList(new byte[]{}, new byte[]{}),
            Collections.singletonList(witnessPlaceholder()));
    Assertions.assertEquals(Calculator.calculateTransactionSize(tx), 464);
  }

  @Test
  void testCalculateTransactionSize2V1() {
    Transaction tx =
        new Transaction(
            0,
            Collections.singletonList(
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0xc12386705b5cbb312b693874f3edf45c43a274482e27b8df0fd80c8d3f5feb8b"),
                        0),
                    CellDep.DepType.DEP_GROUP)),
            Collections.emptyList(),
            Arrays.asList(
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65"),
                        1)),
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65"),
                        1))),
            Collections.singletonList(
                new CellOutput(
                    Long.valueOf("174876e800", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE))),
            Collections.singletonList(new byte[]{}),
            Arrays.asList(witnessPlaceholder(), new byte[]{}));
    Assertions.assertEquals(Calculator.calculateTransactionSize(tx), 407);
  }

  @Test
  void testCalculateTransactionSize2V2() {
    Transaction tx =
        new Transaction(
            0,
            Collections.singletonList(
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0xc12386705b5cbb312b693874f3edf45c43a274482e27b8df0fd80c8d3f5feb8b"),
                        0),
                    CellDep.DepType.DEP_GROUP)),
            Collections.emptyList(),
            Arrays.asList(
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65"),
                        1)),
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray(
                            "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65"),
                        1))),
            Arrays.asList(
                new CellOutput(
                    Long.valueOf("174876e800", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE)),
                new CellOutput(
                    Long.valueOf("174876e800", 16),
                    createScript(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.HashType.TYPE))),
            Arrays.asList(new byte[]{}, new byte[]{}),
            Arrays.asList(witnessPlaceholder(), witnessPlaceholder()));
    Assertions.assertEquals(Calculator.calculateTransactionSize(tx), 601);
  }

  public byte[] witnessPlaceholder() {
    WitnessArgs witnessArgs = new WitnessArgs();
    witnessArgs.setLock(new byte[65]);
    return witnessArgs.pack().toByteArray();
  }
}
