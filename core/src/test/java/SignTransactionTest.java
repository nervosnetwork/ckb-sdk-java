import static org.nervos.ckb.signature.TransactionSigner.TESTNET_TRANSACTION_SIGNER;

import java.util.HashSet;
import java.util.Set;
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
    transactionSigner.register(
        "0x00000000000000",
        "type",
        new ScriptSigner() {
          @Override
          public boolean canSign(String scriptArgs, Context context) {
            return false;
          }

          @Override
          public void signTx(Transaction transaction, ScriptGroup scriptGroup, Object context) {}
        });
    transactionSigner.signTx(transaction);
    // you can reuse transactionSigner
    TransactionWithScriptGroups transaction2 = getTransactionWithScriptGroupsFromMercury();
    transactionSigner.signTx(transaction2);
  }

  private TransactionWithScriptGroups getTransactionWithScriptGroupsFromMercury() {
    return new TransactionWithScriptGroups();
  }
}
