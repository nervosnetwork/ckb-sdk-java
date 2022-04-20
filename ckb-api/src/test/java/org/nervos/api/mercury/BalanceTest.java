package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;
import org.nervos.mercury.model.GetBalancePayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.GetBalanceResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceTest {
  @Test
  void getCkbBalance() throws IOException {
    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.addAssetInfo(AssetInfo.newCkbAsset());

    GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
    Assertions.assertEquals(2, balance.balances.size());
  }

  @Test
  void getSudtBalance() throws IOException {
    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
    Assertions.assertEquals(2, balance.balances.size());
    assertNotNull(balance, "Balance is not empty");
  }

  @Test
  void getAllBalance() throws IOException {
    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));

    GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
    assertNotNull(balance, "Balance is not empty");
    Assertions.assertNotEquals(0, balance.balances.size());
  }

  @Test
  void getBalanceByAddress() throws IOException {
    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress4()));
    builder.addAssetInfo(AssetInfo.newCkbAsset());

    GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
    Assertions.assertNotEquals(0, balance.balances.size());
  }

  @Test
  void getBalanceByIdentity() throws IOException {
    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
    Assertions.assertNotEquals(0, balance.balances.size());
  }

  @Test
  void getBalanceByRecordByScriptByChequeCellSender() throws IOException {
    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(
        ItemFactory.newOutPointItem(
            Numeric.hexStringToByteArray(
                "0x52b1cf0ad857d53e1a3552944c1acf268f6a6aea8e8fc85fe8febcb8127d56f0"),
            0));
    builder.addAssetInfo(AssetInfo.newCkbAsset());

    GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
    Assertions.assertNotEquals(0, balance.balances.size());
  }

  @Test
  void getBalanceByRecordByScriptChequeCellReceiver() throws IOException {
    GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
    builder.item(
        ItemFactory.newOutPointItem(
            Numeric.hexStringToByteArray(
                "0x52b1cf0ad857d53e1a3552944c1acf268f6a6aea8e8fc85fe8febcb8127d56f0"),
            0));
    builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    GetBalanceResponse balance = ApiFactory.getApi().getBalance(builder.build());
    Assertions.assertNotEquals(0, balance.balances.size());
  }
}
