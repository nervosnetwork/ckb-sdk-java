package mercury;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.AdjustAccountPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public class BuildAdjustAccountTest {

  Gson g = new Gson();

  @Test
  void testCreateAssetAccount() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      System.out.println(new Gson().toJson(s));

      Transaction tx = SignUtils.sign(s);

      System.out.println(g.toJson(tx));
      String txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
