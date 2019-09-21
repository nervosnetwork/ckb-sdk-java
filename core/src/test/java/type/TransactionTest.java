package type;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.exceptions.InvalidNumberOfWitnessesException;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellDep;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
class TransactionTest {

  @Test
  void testSign() {
    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            "100000000000",
            new Script(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                Collections.singletonList("0xe2193df51d78411601796b35b17b4f8f2cd85bd0"))));
    cellOutputs.add(
        new CellOutput(
            "4900000000000",
            new Script(
                "0xe3b513a2105a5d4f833d1fad3d968b96b4510687234cd909f86b3ac450d8a2b5",
                Collections.singletonList("0x36c329ed630d6ce750712a477543672adab57f4c"))));

    Transaction tx =
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
            Collections.singletonList(new Witness(Collections.emptyList())));

    BigInteger privateKey =
        Numeric.toBigInt("0xe79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    Transaction signedTx = tx.sign(privateKey);
    Assertions.assertEquals(signedTx.witnesses.size(), tx.inputs.size());
    Assertions.assertEquals(
        signedTx.witnesses.get(0).data.get(0),
        "0xb6acbe173f69ac1ce6f88c6792cd657eccd8d94732ec86a2d8f965771fc62ac01524fb068242cded51dee87a2fd7b4606747a2960ea07539b2233d427c0d86f501");
  }

  @Test
  void testMultipleInputsSign() {
    List<CellInput> cellInputs = new ArrayList<>();
    cellInputs.add(
        new CellInput(
            new OutPoint("0x91fcfd61f420c1090aeded6b6d91d5920a279fe53ec34353afccc59264eeddd4", "0"),
            "113"));
    cellInputs.add(
        new CellInput(
            new OutPoint("0x00000000000000000000000000004e4552564f5344414f494e50555430303031", "0"),
            "0"));

    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            "10000009045634",
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
                new CellDep(
                    new OutPoint(
                        "0xbffab7ee0a050e2cb882de066d3dbf3afdd8932d6a26eda44f06e4b23f0f4b5a", "1"),
                    CellDep.DEP_GROUP)),
            Collections.singletonList("0x"),
            cellInputs,
            cellOutputs,
            Collections.singletonList("0x"),
            witnesses);

    BigInteger privateKey =
        Numeric.toBigInt("0xe79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    Transaction signedTx = tx.sign(privateKey);
    Assertions.assertEquals(signedTx.witnesses.size(), tx.inputs.size());

    List<String> expectedData = new ArrayList<>();
    expectedData.add(
        "0xc612efdce8af3526f20a3a39b623aa8d4fa9136c332d44690b928f2f566feb1f0f550399c9e59456c54c6328f4e77e7f82956c510da3788bba006dbda11ffb5800");
    expectedData.add("0x4107bd23eedb9f2a2a749108f6bb9720d745d50f044cc4814bafe189a01fe6fb");
    Assertions.assertEquals(signedTx.witnesses.get(0).data, expectedData);

    expectedData = new ArrayList<>();
    expectedData.add(
        "0xdd770d3c286573d49613ce2cbf2eeade7a603c157cff212ca10e16e8f88e1aa1358e0e245e55391da779aa6de987404eaafbe4f9c9cd73af963ca01f9499917001");
    Assertions.assertEquals(signedTx.witnesses.get(1).data, expectedData);
  }

  @Test
  void testThrowErrorWhenWitnessesUnsatisfied() {
    List<CellOutput> cellOutputs = new ArrayList<>();
    cellOutputs.add(
        new CellOutput(
            "100000000000",
            new Script(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                Collections.singletonList("0xe2193df51d78411601796b35b17b4f8f2cd85bd0"))));
    cellOutputs.add(
        new CellOutput(
            "4900000000000",
            new Script(
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                Collections.singletonList("0x36c329ed630d6ce750712a477543672adab57f4c"))));

    Transaction tx =
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
            Collections.singletonList("0x"),
            Collections.emptyList());

    BigInteger privateKey =
        Numeric.toBigInt("0xe79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    Assertions.assertThrows(InvalidNumberOfWitnessesException.class, () -> tx.sign(privateKey));
  }

  @Test
  public void serializationTest() {
    Transaction tx =
        new Transaction(
            "0",
            Arrays.asList(
                new CellDep(
                    new OutPoint(
                        "0xc12386705b5cbb312b693874f3edf45c43a274482e27b8df0fd80c8d3f5feb8b", "0"),
                    CellDep.DEP_GROUP),
                new CellDep(
                    new OutPoint(
                        "0x0fb4945d52baf91e0dee2a686cdd9d84cad95b566a1d7409b970ee0a0f364f60", "2"),
                    CellDep.CODE)),
            Collections.emptyList(),
            Collections.singletonList(
                new CellInput(
                    new OutPoint(
                        "0x31f695263423a4b05045dd25ce6692bb55d7bba2965d8be16b036e138e72cc65", "1"),
                    "0")),
            Arrays.asList(
                new CellOutput(
                    "100000000000",
                    new Script(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        Collections.singletonList("0x59a27ef3ba84f061517d13f42cf44ed020610061"),
                        Script.TYPE),
                    new Script(
                        "0xece45e0979030e2f8909f76258631c42333b1e906fd9701ec3600a464a90b8f6",
                        Collections.emptyList(),
                        Script.DATA)),
                new CellOutput(
                    "98824000000000",
                    new Script(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        Collections.singletonList("0x59a27ef3ba84f061517d13f42cf44ed020610061"),
                        Script.TYPE))),
            Arrays.asList("0x", "0x"),
            Collections.singletonList(
                new Witness(
                    Collections.singletonList(
                        "0x82df73581bcd08cb9aa270128d15e79996229ce8ea9e4f985b49fbf36762c5c37936caf3ea3784ee326f60b8992924fcf496f9503c907982525a3436f01ab32900"))));

    Assertions.assertEquals(
        "0x09ce2223304a5f48d5ce2b6ee2777d96503591279671460fa39ae894ea9e2b87", tx.computeHash());
  }

  @Disabled
  @Test
  public void serializationTxTest() throws IOException {
    CKBService ckbService = CKBService.build(new HttpService("http://localhost:8114"));
    Transaction transaction =
        ckbService.getBlockByNumber("1").send().getBlock().transactions.get(0);
    Assertions.assertEquals(
        ckbService.computeTransactionHash(transaction).send().getTransactionHash(),
        transaction.computeHash());
  }
}
