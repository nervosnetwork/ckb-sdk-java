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
}
