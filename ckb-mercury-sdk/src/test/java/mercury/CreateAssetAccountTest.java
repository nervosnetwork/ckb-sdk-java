package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.CreateAssetAccountPayloadBuilder;
import org.nervos.mercury.model.resp.MercuryScriptGroup;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public class CreateAssetAccountTest {

  Gson g = new Gson();

  @Test
  void testCreateAssetAccount() {
    CreateAssetAccountPayloadBuilder builder = new CreateAssetAccountPayloadBuilder();
    builder.keyAddress(AddressWithKeyHolder.testAddress4());
    builder.addUdtHash("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd");

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetAccountCreationTransaction(builder.build());

      System.out.println(new Gson().toJson(s));

      List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
      Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.txView);

      for (MercuryScriptGroup sg : scriptGroups) {
        signBuilder.sign(sg, AddressWithKeyHolder.getKey(sg.pubKey));
      }

      Transaction tx = signBuilder.buildTx();

      System.out.println(g.toJson(tx));
      String txHash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
