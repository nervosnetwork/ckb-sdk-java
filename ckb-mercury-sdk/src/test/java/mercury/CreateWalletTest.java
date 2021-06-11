package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import model.CreateWalletPayloadBuilder;
import model.WalletInfo;
import model.resp.MercuryScriptGroup;
import model.resp.TransactionCompletionResponse;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.transaction.Transaction;

public class CreateWalletTest {

  Gson g = new Gson();

  @Test
  void CreateWallet() {
    CreateWalletPayloadBuilder builder = new CreateWalletPayloadBuilder();
    builder.fee(new BigInteger("1000000"));
    builder.ident(AddressWithKeyHolder.testAddress4());
    builder.addWalletInfo(
        new WalletInfo("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildWalletCreationTransaction(builder.build());

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
