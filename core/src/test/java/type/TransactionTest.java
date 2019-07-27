package type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.exceptions.InvalidNumberOfWitnessesException;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutPoint;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
class TransactionTest {

  @Test
  void testSign() {
    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            "100000000000",
            "0x",
            new Script(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                Collections.singletonList("0xe2193df51d78411601796b35b17b4f8f2cd85bd0"))));
    cellOutputs.add(
        new CellOutput(
            "4900000000000",
            "0x",
            new Script(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                Collections.singletonList("0x36c329ed630d6ce750712a477543672adab57f4c"))));

    Transaction tx =
        new Transaction(
            "0",
            Collections.singletonList(
                new OutPoint(
                    null,
                    new CellOutPoint(
                        "0xbffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a",
                        "1"))),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        null,
                        new CellOutPoint(
                            "0xa80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b50",
                            "0")),
                    "0")),
            cellOutputs,
            Collections.singletonList(new Witness(Collections.emptyList())));

    BigInteger privateKey =
        Numeric.toBigInt("0xe79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    String txHash = "0xac1bb95455cdfb89b6e977568744e09b6b80e08cab9477936a09c4ca07f5b8ab";
    Transaction signedTx = tx.sign(privateKey, txHash);
    Assertions.assertEquals(signedTx.hash, txHash);
    Assertions.assertEquals(signedTx.witnesses.size(), tx.inputs.size());
    Assertions.assertEquals(
        signedTx.witnesses.get(0).data.get(0),
        "0x2c643579e47045be050d3842ed9270151af8885e33954bddad0e53e81d1c2dbe2dc637877a8302110846ebc6a16d9148c106e25f945063ad1c4d4db2b695240800");
  }

  @Test
  void testMultipleInputsSign() {
    List<CellInput> cellInputs = new ArrayList<>();
    cellInputs.add(
        new CellInput(
            new OutPoint(
                "0x23abf65800d048ed9e3eb67e0258e0d616148e9cb1116ceee532a202b4c30e09",
                new CellOutPoint(
                    "0x91fcfd61f420c1090aeded6b6d91d5920a279fe53ec34353afccc59264eeddd4", "0")),
            "113"));
    cellInputs.add(
        new CellInput(
            new OutPoint(
                null,
                new CellOutPoint(
                    "0x00000000000000000000000000004e4552564f5344414f494e50555430303031", "0")),
            "0"));

    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            "10000009045634",
            "0x",
            new Script(
                "0xf1951123466e4479842387a66fabfd6b65fc87fd84ae8e6cd3053edb27fff2fd",
                Collections.singletonList("0x36c329ed630d6ce750712a477543672adab57f4c"))));

    List<Witness> witnesses = new ArrayList<>();
    witnesses.add(
        new Witness(
            Collections.singletonList(
                "0x4107bd23eedb9f2a2a749108f6bb9720d745d50f044cc4814bafe189a01fe6fb")));
    witnesses.add(new Witness(Collections.emptyList()));

    Transaction tx =
        new Transaction(
            "0",
            Collections.singletonList(
                new OutPoint(
                    "0x4107bd23eedb9f2a2a749108f6bb9720d745d50f044cc4814bafe189a01fe6fb", null)),
            cellInputs,
            cellOutputs,
            witnesses);

    BigInteger privateKey =
        Numeric.toBigInt("0xe79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    String txHash = "0x985772e541c23d4e7dbf9844a9b9d93fcdc62273fa1f4ae1ae82703962dc1a4e";
    Transaction signedTx = tx.sign(privateKey, txHash);
    Assertions.assertEquals(signedTx.hash, txHash);
    Assertions.assertEquals(signedTx.witnesses.size(), tx.inputs.size());

    List<String> expectedData = new ArrayList<>();
    expectedData.add(
        "0x68a57373f4e98aecfb9501ec1cc4a78c048361332e4b6706bdc1469d30bd52ea42feca657dd1de1eff384e6ed24a6910b011d49d855bd1ed209f5ce77d8116ac01");
    expectedData.add("0x4107bd23eedb9f2a2a749108f6bb9720d745d50f044cc4814bafe189a01fe6fb");
    Assertions.assertEquals(signedTx.witnesses.get(0).data, expectedData);

    expectedData = new ArrayList<>();
    expectedData.add(
        "0x3b13c362f254e7becb0e731e4756e742bfddbf2f5d7c16cd609ba127d2b7e07f1d588c3a7132fc20c478e2de14f6370fbb9e4402d240e4b32c8d671177e1f31101");
    Assertions.assertEquals(signedTx.witnesses.get(1).data, expectedData);
  }

  @Test
  void testThrowErrorWhenWitnessesUnsatisfied() {
    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            "100000000000",
            "0x",
            new Script(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                Collections.singletonList("0xe2193df51d78411601796b35b17b4f8f2cd85bd0"))));
    cellOutputs.add(
        new CellOutput(
            "4900000000000",
            "0x",
            new Script(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                Collections.singletonList("0x36c329ed630d6ce750712a477543672adab57f4c"))));

    Transaction tx =
        new Transaction(
            "0",
            Collections.singletonList(
                new OutPoint(
                    null,
                    new CellOutPoint(
                        "0xbffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a",
                        "1"))),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        null,
                        new CellOutPoint(
                            "0xa80a8e01d45b10e1cbc8a2557c62ba40edbdc36cd63a31fc717006ca7b157b50",
                            "0")),
                    "0")),
            cellOutputs,
            Collections.emptyList());

    BigInteger privateKey =
        Numeric.toBigInt("0xe79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    String txHash = "0xac1bb95455cdfb89b6e977568744e09b6b80e08cab9477936a09c4ca07f5b8ab";
    Assertions.assertThrows(
        InvalidNumberOfWitnessesException.class, () -> tx.sign(privateKey, txHash));
  }
}
