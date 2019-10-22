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
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
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
                "0xe2193df51d78411601796b35b17b4f8f2cd85bd0")));
    cellOutputs.add(
        new CellOutput(
            "4900000000000",
            new Script(
                "0xe3b513a2105a5d4f833d1fad3d968b96b4510687234cd909f86b3ac450d8a2b5",
                "0x36c329ed630d6ce750712a477543672adab57f4c")));

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
            Collections.singletonList("0x"));

    BigInteger privateKey =
        Numeric.toBigInt("0xe79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3");
    Transaction signedTx = tx.sign(privateKey);
    Assertions.assertEquals(signedTx.witnesses.size(), tx.inputs.size());
    Assertions.assertEquals(
        signedTx.witnesses.get(0),
        "0x74e6007907aaeacf9bf3671a352d0c6cd105f1a2e6b406a33301fb5dfa5246f927538dcd25c2e68e1096b380ef621ff17a5fdcfd4cc5345b7534239c72b177e301");
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
                "0x36c329ed630d6ce750712a477543672adab57f4c")));

    List<String> witnesses = new ArrayList<>();
    witnesses.add("0x4107bd23eedb9f2a2a749108f6bb9720d745d50f044cc4814bafe189a01fe6fb");
    witnesses.add("0x");

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
        "0x8dbb53f6326240110e67c8f331140a615b37a67de5e6479fbdf4f9fb5789eaf946226a47a9c92c502b5f45b43717611a31f913f49b164846f510c92eeef69c76004107bd23eedb9f2a2a749108f6bb9720d745d50f044cc4814bafe189a01fe6fb");
    expectedData.add(
        "0x833210c0282ec82ce1064399547d536acaea28df17b691886c80701cb18230cf1d536aaaab6cc5e3faa5d949383cfd5c082fef37499e3d120d6144a9d5ad84d900");
    Assertions.assertEquals(signedTx.witnesses, expectedData);
  }

  @Test
  void testThrowErrorWhenWitnessesUnsatisfied() {
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
                "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08",
                "0x36c329ed630d6ce750712a477543672adab57f4c")));

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
    Assertions.assertThrows(RuntimeException.class, () -> tx.sign(privateKey));
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
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.TYPE),
                    new Script(
                        "0xece45e0979030e2f8909f76258631c42333b1e906fd9701ec3600a464a90b8f6",
                        "0x",
                        Script.DATA)),
                new CellOutput(
                    "98824000000000",
                    new Script(
                        "0x68d5438ac952d2f584abf879527946a537e82c7f3c1cbf6d8ebf9767437d8e88",
                        "0x59a27ef3ba84f061517d13f42cf44ed020610061",
                        Script.TYPE))),
            Arrays.asList("0x", "0x"),
            Collections.singletonList(
                "0x82df73581bcd08cb9aa270128d15e79996229ce8ea9e4f985b49fbf36762c5c37936caf3ea3784ee326f60b8992924fcf496f9503c907982525a3436f01ab32900"));

    Assertions.assertEquals(
        "0xe765f9912b06c72552dae11779f6371309236e968aa045ae3b8f426d8ec8ca05", tx.computeHash());
  }

  @Disabled
  @Test
  public void serializationTxTest() throws IOException {
    Api api = new Api("http://localhost:8114");
    Transaction transaction = api.getBlockByNumber("1").transactions.get(0);
    Assertions.assertEquals(api.computeTransactionHash(transaction), transaction.computeHash());
  }
}
