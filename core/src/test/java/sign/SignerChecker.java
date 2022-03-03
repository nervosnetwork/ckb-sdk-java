package sign;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.nervos.ckb.sign.TransactionSigner.TESTNET_TRANSACTION_SIGNER;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;

public class SignerChecker {
  @SerializedName("raw_transaction")
  private TransactionWithScriptGroups transaction;

  @SerializedName("expected_witnesses")
  private List<String> expectedWitnesses;

  @SerializedName("private_keys")
  private List<String> privateKeys;

  private SignerChecker() {}

  private static SignerChecker fromFile(String fileName) {
    try (Reader reader = readTransactionJsonFile(fileName)) {
      Gson gson = new Gson();
      return gson.fromJson(reader, SignerChecker.class);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  private static Reader readTransactionJsonFile(String fileName) throws IOException {
    String filePath = "src/test/resources/transaction/" + fileName + ".json";
    return Files.newBufferedReader(Paths.get(filePath));
  }

  public static void signAndCheck(String fileName) {
    SignerChecker checker = SignerChecker.fromFile(fileName);
    checker.signAndCheck();
  }

  private void signAndCheck() {
    Set<Integer> signedGroupsIndices = signTransaction(TESTNET_TRANSACTION_SIGNER);
    assertEquals(this.transaction.getScriptGroups().size(), signedGroupsIndices.size());
    for (int i = 0; i < this.transaction.getScriptGroups().size(); i++) {
      assertEquals(true, signedGroupsIndices.contains(i));
    }
    check();
  }

  private Set<Integer> signTransaction(TransactionSigner transactionSigner) {
    String[] privateKeysArray = this.privateKeys.toArray(new String[this.privateKeys.size()]);
    return transactionSigner.signTransaction(transaction, privateKeysArray);
  }

  private void check() {
    List<String> witnesses = transaction.getTxView().witnesses;
    assertNotEquals(0, witnesses.size());
    assertEquals(expectedWitnesses.size(), witnesses.size());
    for (int i = 0; i < expectedWitnesses.size(); i++) {
      assertEquals(expectedWitnesses.get(i), witnesses.get(i));
    }
  }
}