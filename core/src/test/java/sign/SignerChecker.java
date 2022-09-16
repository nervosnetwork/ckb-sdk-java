package sign;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.Network;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.sign.omnilock.OmnilockConfig;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SignerChecker {
  @SerializedName("raw_transaction")
  private TransactionWithScriptGroups transaction;
  private List<String> expectedWitnesses;
  private Set<Context> contexts;

  private SignerChecker() {
  }

  private static Gson getGson() {
    JsonDeserializer typeAdapter = (JsonDeserializer<Context>) (json, typeOfT, context) -> {
      JsonObject obj = json.getAsJsonObject();
      ECKeyPair keyPair = ECKeyPair.create(obj.get("private_key").getAsString());
      Context c = new Context(keyPair);
      if (obj.get("multisig_script") != null) {
        JsonObject obj2 = obj.get("multisig_script").getAsJsonObject();
        int threshold = obj2.get("threshold").getAsInt();
        int firstN = obj2.get("first_n").getAsInt();
        List<byte[]> keyHashes = new ArrayList<>();
        for (JsonElement e: obj2.get("key_hashes").getAsJsonArray()) {
          keyHashes.add(Numeric.hexStringToByteArray(e.getAsString()));
        }
        Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript =
            new Secp256k1Blake160MultisigAllSigner.MultisigScript(firstN, threshold, keyHashes);
        c.setPayload(multisigScript);
      } else if (obj.get("omnilock_config") != null) {
        JsonObject obj2 = obj.get("omnilock_config").getAsJsonObject();
        byte[] args = Numeric.hexStringToByteArray(obj2.get("args").getAsString());
        OmnilockConfig.Mode mode = OmnilockConfig.Mode.valueOf(obj2.get("mode").getAsString());
        OmnilockConfig omnilockConfig = new OmnilockConfig(args, mode);
        c.setPayload(omnilockConfig);
      }
      return c;
    };
    Gson gson = GsonFactory.create()
        .newBuilder()
        .registerTypeAdapter(Context.class, typeAdapter)
        .create();
    return gson;
  }

  private static SignerChecker fromFile(String fileName) {
    try (Reader reader = readTransactionJsonFile(fileName)) {
      Gson gson = getGson();
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
    TransactionSigner txSigner = TransactionSigner.getInstance(Network.TESTNET);
    Set<Integer> signedGroupsIndices = signTransaction(txSigner);
    assertEquals(this.transaction.getScriptGroups().size(), signedGroupsIndices.size());
    for (int i = 0; i < this.transaction.getScriptGroups().size(); i++) {
      assertEquals(true, signedGroupsIndices.contains(i));
    }
    check();
  }

  private Set<Integer> signTransaction(TransactionSigner transactionSigner) {
    return transactionSigner.signTransaction(transaction, contexts);
  }

  private void check() {
    List<byte[]> witnesses = transaction.getTxView().witnesses;
    assertNotEquals(0, witnesses.size());
    assertEquals(expectedWitnesses.size(), witnesses.size());
    for (int i = 0; i < expectedWitnesses.size(); i++) {
      assertArrayEquals(Numeric.hexStringToByteArray(expectedWitnesses.get(i)), witnesses.get(i));
    }
  }
}
