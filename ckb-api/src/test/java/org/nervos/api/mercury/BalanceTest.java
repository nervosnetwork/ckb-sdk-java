package org.nervos.api.mercury;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.indexer.model.ScriptType;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;
import org.nervos.mercury.GsonFactory;
import org.nervos.mercury.model.GetBalancePayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.GetBalanceResponse;

public class BalanceTest {
  Gson g = GsonFactory.newGson();

  @Test
  void getCkbBalance() {
    try {

      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
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
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
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
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));

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
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress4()));
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
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
      builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void getBalanceByRecordByScriptByChequeCellSender() {

    try {

      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(
          ItemFactory.newOutPointItem(
                  Numeric.hexStringToByteArray("0x52b1cf0ad857d53e1a3552944c1acf268f6a6aea8e8fc85fe8febcb8127d56f0"), 0));
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
  void getBalanceByRecordByScriptChequeCellReceiver() {

    try {
      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(
          ItemFactory.newOutPointItem(
                  Numeric.hexStringToByteArray("0x52b1cf0ad857d53e1a3552944c1acf268f6a6aea8e8fc85fe8febcb8127d56f0"), 0));
      builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

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
      builder.item(ItemFactory.newOutPointItem(cells.outPoint.txHash, cells.outPoint.index));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(g.toJson(builder.build()));

      GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
      assertNotNull(balance, "Balance is not empty");
      System.out.println(g.toJson(balance));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private CellResponse getCells(Script script) throws IOException {

    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(script);
    key.scriptType(ScriptType.lock);

    System.out.println(g.toJson(key.build()));

    CellsResponse cells =
        ApiFactory.getApi()
            .getCells(key.build(), "asc", "0x" + new BigInteger("10").toString(16), null);

    if (cells.objects.size() > 0) {
      return cells.objects.get(0);
    }
    return null;
  }
}
