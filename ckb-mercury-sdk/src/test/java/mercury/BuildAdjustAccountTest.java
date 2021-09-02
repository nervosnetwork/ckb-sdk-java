package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.AdjustAccountPayloadBuilder;
import org.nervos.mercury.model.resp.AssetInfo;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public class BuildAdjustAccountTest {

  Gson g = new Gson();

  @Test
  void testBuildAdjustAccount() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.address(AddressWithKeyHolder.testAddress4());
    builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      System.out.println(new Gson().toJson(s));

      Transaction tx = SignUtils.sign(s);

      System.out.println(g.toJson(tx));
      String txHash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
