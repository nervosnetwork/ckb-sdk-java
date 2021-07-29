package org.nervos.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.CkbRpcApi;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Header;
import org.nervos.indexer.CkbIndexerApi;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.model.Script;
import org.nervos.indexer.model.ScriptType;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.mercury.DefaultMercuryApi;
import org.nervos.mercury.MercuryApi;
import org.nervos.mercury.model.GetBalancePayloadBuilder;
import org.nervos.mercury.model.req.KeyAddress;
import org.nervos.mercury.model.req.NormalAddress;
import org.nervos.mercury.model.req.QueryAddress;
import org.nervos.mercury.model.resp.GetBalanceResponse;

/** @author zjh @Created Date: 2021/7/25 @Description: @Modify by: */
public class CkbApiTest {

  private String rpcAddress = "http://127.0.0.1:8116";
  Gson g =
      new GsonBuilder()
          .registerTypeAdapter(QueryAddress.class, new KeyAddress(""))
          .registerTypeAdapter(QueryAddress.class, new NormalAddress(""))
          .create();

  @Test
  void testCkbIndexerApi() {
    CkbIndexerApi api = new DefaultIndexerApi(rpcAddress, false);
    indexerApi(api);
  }

  @Test
  void testCkbApi() {
    CkbRpcApi api = new Api(rpcAddress, false);
    ckbApi(api);
  }

  @Test
  void testMercuryApi() {
    MercuryApi api = new DefaultMercuryApi(rpcAddress, false);
    mercuryApi(api);
  }

  @Test
  void testAll() {
    CkbApi api = new DefaultCkbApi(rpcAddress, false);
    ckbApi(api);
    indexerApi(api);
    mercuryApi(api);
  }

  private void indexerApi(CkbIndexerApi api) {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "type",
            "0x0c24d18f16e3c43272695e5db006a22cb9ddde51"));
    key.scriptType(ScriptType.lock);

    System.out.println(new Gson().toJson(key.build()));

    try {
      CellCapacityResponse capacity = api.getCellsCapacity(key.build());
      System.out.println(new Gson().toJson(capacity));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void ckbApi(CkbRpcApi api) {
    try {
      Header tipHeader = api.getTipHeader();
      System.out.println(new Gson().toJson(tipHeader));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void mercuryApi(MercuryApi api) {
    try {

      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.address(new KeyAddress("ckt1qyq28wze3cw48ek9az0g4jmtfs6d8td38u4s6hp2s0"));

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = api.getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
