package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.crypto.secp256k1.Keys;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.address.Address;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.SimpleTransferPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

public class BuildSimpleTransferTransactionTest {

  @Test
  void testCkbInsufficientBalanceToPayTheFee1() {
    try {
      Address from = new Address(Script.generateSecp256K1Blake160SignhashAllScript(Keys.createEcKeyPair()), Network.TESTNET);
      Address to = new Address(Script.generateSecp256K1Blake160SignhashAllScript(Keys.createEcKeyPair()), Network.TESTNET);

      SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
      builder.addFrom(from.encode());
      builder.addTo(new ToInfo(to.encode(), AmountUtils.ckbToShannon(100)));
      builder.assetInfo(AssetInfo.newCkbAsset());

      ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

    } catch (Exception e) {
      e.printStackTrace();
      //      assertEquals(true, e.getMessage().contains("token is not enough"));
    }
  }

  @Test
  void testCkbInsufficientBalanceToPayTheFee2() {
    try {
      Address from = new Address(Script.generateSecp256K1Blake160SignhashAllScript(Keys.createEcKeyPair()), Network.TESTNET);

      SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
      builder.addFrom(from.encode());
      builder.addTo(new ToInfo(AddressWithKeyHolder.testAddress4(), AmountUtils.ckbToShannon(100)));
      builder.assetInfo(AssetInfo.newCkbAsset());

      ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

    } catch (Exception e) {
      e.printStackTrace();
      //      assertEquals(true, e.getMessage().contains("token is not enough"));
    }
  }

  @Test
  void testSourceByClaimable() {

    SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
    builder.addFrom(AddressWithKeyHolder.testAddress2());
    builder.addTo(new ToInfo(AddressWithKeyHolder.testAddress4(), 20));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    try {

      TransactionCompletionResponse transactionCompletionResponse =
          ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      byte[] hash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSourceByChequeCell() {

    try {
      Address to = new Address(Script.generateSecp256K1Blake160SignhashAllScript(Keys.createEcKeyPair()), Network.TESTNET);

      SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
      builder.addFrom(AddressWithKeyHolder.testAddress2());
      builder.addTo(new ToInfo(to.encode(), 20));
      builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

      TransactionCompletionResponse transactionCompletionResponse =
          ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      byte[] hash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSourceByFree() {

    SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
    builder.addFrom(AddressWithKeyHolder.testAddress4());
    builder.addTo(new ToInfo(AddressWithKeyHolder.testAddress1(), 20));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    try {
      TransactionCompletionResponse transactionCompletionResponse =
          ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      byte[] hash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testModeByHoldyTo() {

    SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
    builder.addFrom(AddressWithKeyHolder.testAddress1());
    builder.addTo(
        new ToInfo(
            AddressTools.generateAcpAddress(AddressWithKeyHolder.testAddress4()).encode(),
            20));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    try {
      TransactionCompletionResponse transactionCompletionResponse =
          ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      byte[] hash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
