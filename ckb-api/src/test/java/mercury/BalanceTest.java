package mercury;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.indexer.model.ScriptType;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;
import org.nervos.mercury.model.GetBalancePayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.resp.GetBalanceResponse;

public class BalanceTest {

  Gson g = new GsonBuilder().create();

  @Test
  void getCkbBalance() {
    try {

      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getSudtBalance() {
    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    System.out.println(g.toJson(builder.build()));

    try {
      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getAllBalance() {

    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));

    System.out.println(g.toJson(builder.build()));

    try {
      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
      System.out.println(balance.balances.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getBalanceByAddress() {

    try {
      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(Item.newAddressItem(AddressWithKeyHolder.testAddress4()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getBalanceByIdentity() {

    try {
      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getBalanceByRecordByScript() {

    try {

      Script script = AddressTools.parse(AddressWithKeyHolder.testAddress4()).script;
      CellResponse cells = getCells(script);

      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(
          Item.newRecordItemByScript(
              new OutPoint(cells.outPoint.txHash, cells.outPoint.index), script));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getBalanceByRecordByAddress() {

    try {

      Script script = AddressTools.parse(AddressWithKeyHolder.testAddress4()).script;
      CellResponse cells = getCells(script);

      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(
          Item.newRecordItemByAddress(
              new OutPoint(cells.outPoint.txHash, cells.outPoint.index),
              AddressWithKeyHolder.testAddress4()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private CellResponse getCells(Script script) throws IOException {

    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(script);
    key.scriptType(ScriptType.lock);

    System.out.println(new Gson().toJson(key.build()));

    CellsResponse cells =
        ApiFactory.getApi()
            .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);

    if (cells.objects.size() > 0) {
      return cells.objects.get(0);
    }
    return null;
  }
}
