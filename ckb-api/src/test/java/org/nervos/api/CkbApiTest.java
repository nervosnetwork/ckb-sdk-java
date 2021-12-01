package org.nervos.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.CkbRpcApi;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.Script;
import org.nervos.indexer.CkbIndexerApi;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.model.ScriptType;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.mercury.DefaultMercuryApi;
import org.nervos.mercury.GsonFactory;
import org.nervos.mercury.MercuryApi;
import org.nervos.mercury.model.GetBalancePayloadBuilder;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.GetBalanceResponse;

/** @author zjh @Created Date: 2021/7/25 @Description: @Modify by: */
public class CkbApiTest {

  private static String MERCURY_URL = "http://127.0.0.1:8116";

  private static String CKB_URL = "https://mercury-testnet.ckbapp.dev";

  private static String Indexer_URL = "https://mercury-testnet.ckbapp.dev";

  Gson g = GsonFactory.newGson();

  @Test
  void testCkbIndexerApi() {
    CkbIndexerApi api = new DefaultIndexerApi(Indexer_URL, false);
    indexerApi(api);
  }

  @Test
  void testCkbApi() {
    CkbRpcApi api = new Api(CKB_URL, false);
    ckbApi(api);
  }

  @Test
  void testMercuryApi() {
    MercuryApi api = new DefaultMercuryApi(MERCURY_URL, false);
    mercuryApi(api);
  }

  @Test
  void testAll() {
    CkbApi api = new DefaultCkbApi(CKB_URL, MERCURY_URL, Indexer_URL, false);
    ckbApi(api);
    indexerApi(api);
    mercuryApi(api);
  }

  private void indexerApi(CkbIndexerApi api) {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51",
            Script.TYPE));
    key.scriptType(ScriptType.lock);

    System.out.println(g.toJson(key.build()));

    try {
      CellCapacityResponse capacity = api.getCellsCapacity(key.build());
      System.out.println(g.toJson(capacity));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void ckbApi(CkbRpcApi api) {
    try {
      Header tipHeader = api.getTipHeader();
      System.out.println(g.toJson(tipHeader));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void mercuryApi(MercuryApi api) {
    try {

      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(
          ItemFactory.newAddressItem(
              "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdrhpvcu82numz73852ed45cdxn4kcn72cr4338a"));

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = api.getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
