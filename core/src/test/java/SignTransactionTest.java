import static org.nervos.ckb.signature.TransactionSigner.TESTNET_TRANSACTION_SIGNER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.signature.*;
import org.nervos.ckb.type.transaction.Transaction;

public class SignTransactionTest {

  @Test
  void simpleExample() {
    TransactionWithScriptGroups transaction = getTransactionWithScriptGroupsFromMercury();

    // #1 one private key to sign tx
    TESTNET_TRANSACTION_SIGNER.signTx(transaction, "0x1234");

    // #2 need different private key to sign tx
    TESTNET_TRANSACTION_SIGNER.signTx(transaction, "0x12345", "0x67890");

    // #3 complex structure for secret to sign tx
    Set contexts = new HashSet();
    contexts.add("0x1234");
    contexts.add(1234);
    TESTNET_TRANSACTION_SIGNER.signTx(transaction, contexts);

    // #4 register your own ScriptSigner
    TransactionSigner transactionSigner = new TransactionSigner(TESTNET_TRANSACTION_SIGNER);
    transactionSigner.registerLockScriptSigner(
        "0x00000000000000",
        new ScriptSigner() {
          @Override
          public boolean signTx(Transaction transaction, ScriptGroup scriptGroup, Context context) {
            return true;
          }
        });
    transactionSigner.signTx(transaction);
    // you can reuse transactionSigner
    TransactionWithScriptGroups transaction2 = getTransactionWithScriptGroupsFromMercury();
    transactionSigner.signTx(transaction2);
  }

  private TransactionWithScriptGroups getTransactionWithScriptGroupsFromMercury() {
    return new TransactionWithScriptGroups();
  }

  @Test
  void testSingleSecp256k1Blake160Script() {
    // This test transaction is from
    // https://explorer.nervos.org/aggron/transaction/0x7be5f1df2c5eb2f33bcf20603774e485c78ab7616e059908715b4a8200e8949f
    Transaction tx =
        Transaction.builder()
            .addCellDep("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", "0x0")
            .addCellDep("0xec26b0f85ed839ece5f11c4c4e837ec359f5adc4420410f6453b1f6b60fb96a6", "0x0")
            .addInput("0xbaf3371f487a0d40f8ebc341a34b93a2d36e1d9f77b9533fb8c579c87958b7aa", "0x0")
            .addWitness(
                "55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
            .addOutput(
                "0x2540be400",
                "0x05a1fabfa84db9e538e2e7fe3ca9adf849f55ce0",
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
            .addOutput(
                "0x368f7cadfb00",
                "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b",
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8")
            .addOutputData("0x")
            .addOutputData("0x")
            .build();

    ScriptGroup scriptGroup =
        ScriptGroup.builder()
            .setScript(
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b")
            .setScriptType(ScriptType.LOCK)
            .addInputIndex(0)
            .build();

    List<ScriptGroup> scriptGroups = new ArrayList<>();
    scriptGroups.add(scriptGroup);
    TransactionWithScriptGroups txWithScriptGroup = new TransactionWithScriptGroups();
    txWithScriptGroup.setTxView(tx);
    txWithScriptGroup.setScriptGroups(scriptGroups);

    String privateKey = "6fc935dad260867c749cf1ba6602d5f5ed7fb1131f1beb65be2d342e912eaafe";
    TESTNET_TRANSACTION_SIGNER.signTx(txWithScriptGroup, privateKey);

    List<String> witnesses = txWithScriptGroup.getTxView().witnesses;
    Assertions.assertEquals(1, witnesses.size());
    Assertions.assertEquals(
        "0x550000001000000055000000550000004100000090b18cc17b8c67e20075ffcffe82d079e0b6a78cb3184157d78962bdd5a648d82c9bc8e1bbe87e7b8b0661440c1060f939be85d26742148e08dc58743a900df401",
        witnesses.get(0));
  }

  @Test
  void testSingleSecp256k1Blake160ScriptInGroup() {
    // This test transaction is from
    // https://explorer.nervos.org/aggron/transaction/0x150ab94cc3d35daf96d0d55a4efc420323adcc36662b2bdcab826e16ce38dd81
    Transaction tx =
        Transaction.builder()
            .addCellDep("0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37", "0x0")
            .addCellDep("0xec26b0f85ed839ece5f11c4c4e837ec359f5adc4420410f6453b1f6b60fb96a6", "0x0")
            .addCellDep(
                "0xe12877ebd2c3c364dc46c5c992bcfaf4fee33fa13eebdf82c591fc9825aab769", "0x0", "code")
            .addInput("0xc43c8198c4ead3dce957cc3a3ab2ca6c8f4c23ad9d74cb083daefd5d2e4fba4e", "0x0")
            .addInput("0x469100c2149317341756e80f369c94ed2a84b58349ff41985819d49413377ae8", "0x0")
            .addWitness(
                "55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
            .addWitness("0x10000000100000001000000010000000")
            .addOutput(
                "0xea46318821",
                "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b",
                "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
                "0xc772f4d885ca6285d87d82b8edc1643df9f3ce63c40d0f81f2a38c147328d430",
                "0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4")
            .addOutputData("0x00000000000000000000000000000000")
            .build();

    ScriptGroup scriptGroup =
        ScriptGroup.builder()
            .setScript(
                "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                "0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b")
            .setScriptType(ScriptType.LOCK)
            .addInputIndex(0)
            .addInputIndex(1)
            .build();

    List<ScriptGroup> scriptGroups = new ArrayList<>();
    scriptGroups.add(scriptGroup);
    TransactionWithScriptGroups txWithScriptGroup = new TransactionWithScriptGroups();
    txWithScriptGroup.setTxView(tx);
    txWithScriptGroup.setScriptGroups(scriptGroups);

    String privateKey = "6fc935dad260867c749cf1ba6602d5f5ed7fb1131f1beb65be2d342e912eaafe";
    TESTNET_TRANSACTION_SIGNER.signTx(txWithScriptGroup, privateKey);

    List<String> witnesses = txWithScriptGroup.getTxView().witnesses;
    Assertions.assertEquals(2, witnesses.size());
    Assertions.assertEquals(
        "0x5500000010000000550000005500000041000000ed0c2ec9523029ed21be22fce92ff158d4da25da0aebd050cdd4b04a9c980ccf5f76afc8d33fa890fcb231bde3eba46b2932d4aaecd4df559ecc3d268d90ef8c01",
        witnesses.get(0));
    Assertions.assertEquals("0x10000000100000001000000010000000", witnesses.get(1));
  }

}
