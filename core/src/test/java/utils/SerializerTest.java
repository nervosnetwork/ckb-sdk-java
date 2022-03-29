package utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

import static utils.TestUtils.createScript;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SerializerTest {

  private Transaction tx;

  @BeforeAll
  void init() {
    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
                new BigInteger("100000000000"),
                createScript(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                "0xe2193df51d78411601796b35b17b4f8f2cd85bd0")));
    cellOutputs.add(
        new CellOutput(
                new BigInteger("4900000000000"),
                createScript(
                "0xe3b513a2105a5d4f833d1fad3d968b96b4510687234cd909f86b3ac450d8a2b5",
                "0x36c329ed630d6ce750712a477543672adab57f4c")));

    tx =
        new Transaction(
            "0",
            Collections.singletonList(
                new CellDep(
                    new OutPoint(
                        Numeric.hexStringToByteArray("0xbffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a"), 1),
                    CellDep.DepType.DEP_GROUP)),
            Collections.singletonList("0x"),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        Numeric.hexStringToByteArray("0xa80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b50"), 0),
                    "0")),
            cellOutputs,
            Arrays.asList("0x", "0x"),
            Collections.singletonList(new Witness()));
  }

  @Test
  public void testSerializeOutPoint() {
    Assertions.assertArrayEquals(
        Serializer.serializeOutPoint(tx.inputs.get(0).previousOutput).toBytes(),
        Numeric.hexStringToByteArray(
            "a80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b5000000000"));
  }

  @Test
  public void testSerializeCellInput() {
    Assertions.assertArrayEquals(
        Serializer.serializeCellInput(tx.inputs.get(0)).toBytes(),
        Numeric.hexStringToByteArray(
            "0000000000000000a80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b5000000000"));
  }

  @Test
  public void testSerializeScript() {
    Assertions.assertArrayEquals(
        Serializer.serializeScript(tx.outputs.get(0).lock).toBytes(),
        Numeric.hexStringToByteArray(
            "490000001000000030000000310000009e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a080014000000e2193df51d78411601796b35b17b4f8f2cd85bd0"));
  }

  @Test
  public void testSerializeCellOutput() {
    Assertions.assertArrayEquals(
        Serializer.serializeCellOutput(tx.outputs.get(0)).toBytes(),
        Numeric.hexStringToByteArray(
            "610000001000000018000000610000000000000000100000490000001000000030000000310000009e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a080014000000e2193df51d78411601796b35b17b4f8f2cd85bd0"));
  }

  @Test
  public void testSerializeCellDep() {
    Assertions.assertArrayEquals(
        Serializer.serializeCellDep(tx.cellDeps.get(0)).toBytes(),
        Numeric.hexStringToByteArray(
            "bffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a0100000001"));
  }

  @Test
  public void testSerializeCellDeps() {
    Assertions.assertArrayEquals(
        Serializer.serializeCellDeps(tx.cellDeps).toBytes(),
        Numeric.hexStringToByteArray(
            "01000000bffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a0100000001"));
  }

  @Test
  public void testSerializeCellInputs() {
    Assertions.assertArrayEquals(
        Serializer.serializeCellInputs(tx.inputs).toBytes(),
        Numeric.hexStringToByteArray(
            "010000000000000000000000a80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b5000000000"));
  }

  @Test
  public void testSerializeCellOutputs() {
    Assertions.assertArrayEquals(
        Serializer.serializeCellOutputs(tx.outputs).toBytes(),
        Numeric.hexStringToByteArray(
            "ce0000000c0000006d000000610000001000000018000000610000000000000000100000490000001000000030000000310000009e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a080014000000e2193df51d78411601796b35b17b4f8f2cd85bd061000000100000001800000061000000000000000090040049000000100000003000000031000000e3b513a2105a5d4f833d1fad3d968b96b4510687234cd909f86b3ac450d8a2b5001400000036c329ed630d6ce750712a477543672adab57f4c"));
  }

  @Test
  public void testSerializeWitnessArgs() {
    Assertions.assertArrayEquals(
        Serializer.serializeWitnessArgs(new Witness("", "0x", "")).toBytes(),
        Numeric.hexStringToByteArray("0x10000000100000001000000010000000"));

    Assertions.assertArrayEquals(
        Serializer.serializeWitnessArgs(new Witness("", "0x10", "0x20")).toBytes(),
        Numeric.hexStringToByteArray("0x1a00000010000000100000001500000001000000100100000020"));

    Assertions.assertArrayEquals(
        Serializer.serializeWitnessArgs(
                new Witness(
                    "0x0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                    "",
                    ""))
            .toBytes(),
        Numeric.hexStringToByteArray(
            "55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
  }

  @Test
  public void testSerializeWitnesses() {
    Assertions.assertArrayEquals(
        Serializer.serializeWitnesses(Collections.emptyList()).toBytes(),
        Numeric.hexStringToByteArray("0x04000000"));

    Assertions.assertArrayEquals(
        Serializer.serializeWitnesses(Arrays.asList("0x10", "0x01")).toBytes(),
        Numeric.hexStringToByteArray("0x160000000c0000001100000001000000100100000001"));

    Assertions.assertArrayEquals(
        Serializer.serializeWitnesses(
                Collections.singletonList("0x3954acece65096bfa81258983ddb83915fc56bd8"))
            .toBytes(),
        Numeric.hexStringToByteArray(
            "0x2000000008000000140000003954acece65096bfa81258983ddb83915fc56bd8"));
  }

  @Test
  public void testSerializeBytes() {
    Assertions.assertArrayEquals(
        Serializer.serializeBytes(tx.outputsData).toBytes(),
        Numeric.hexStringToByteArray("140000000c000000100000000000000000000000"));
  }

  @Test
  public void testSerializeByte32() {
    Assertions.assertArrayEquals(
        Serializer.serializeByte32(tx.headerDeps).toBytes(),
        Numeric.hexStringToByteArray(
            "010000000000000000000000000000000000000000000000000000000000000000000000"));
  }

  @Test
  public void testSerializeRawTransaction() {
    Assertions.assertArrayEquals(
        Serializer.serializeRawTransaction(tx).toBytes(),
        Numeric.hexStringToByteArray(
            "7f0100001c00000020000000490000006d0000009d0000006b0100000000000001000000bffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a0100000001010000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000a80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b5000000000ce0000000c0000006d0000006100000010000000180000006100000000e8764817000000490000001000000030000000310000009e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a080014000000e2193df51d78411601796b35b17b4f8f2cd85bd0610000001000000018000000610000000068c2de7404000049000000100000003000000031000000e3b513a2105a5d4f833d1fad3d968b96b4510687234cd909f86b3ac450d8a2b5001400000036c329ed630d6ce750712a477543672adab57f4c140000000c000000100000000000000000000000"));
  }

  @Test
  public void testSerializeTransaction() {
    BigInteger privateKey =
        Numeric.toBigInt("0xe79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    Transaction signedTx = tx.sign(privateKey);

    System.out.println(
        Numeric.toHexStringNoPrefix(Serializer.serializeTransaction(signedTx).toBytes()));

    Assertions.assertArrayEquals(
        Serializer.serializeTransaction(signedTx).toBytes(),
        Numeric.hexStringToByteArray(
            "ec0100000c0000008b0100007f0100001c00000020000000490000006d0000009d0000006b0100000000000001000000bffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a0100000001010000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000a80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b5000000000ce0000000c0000006d0000006100000010000000180000006100000000e8764817000000490000001000000030000000310000009e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a080014000000e2193df51d78411601796b35b17b4f8f2cd85bd0610000001000000018000000610000000068c2de7404000049000000100000003000000031000000e3b513a2105a5d4f833d1fad3d968b96b4510687234cd909f86b3ac450d8a2b5001400000036c329ed630d6ce750712a477543672adab57f4c140000000c000000100000000000000000000000610000000800000055000000550000001000000055000000550000004100000010f86974898b2f3685facb78741801bf2b932c7c548afe5bbc5d06ee135aeb792d700a02b62c492f1fd6e88afd655ffe305489fe9a76670a8999c641c8e2b16701"));
  }
}
