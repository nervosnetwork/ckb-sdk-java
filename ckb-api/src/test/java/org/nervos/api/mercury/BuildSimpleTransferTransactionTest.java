package org.nervos.api.mercury;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
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
      AddressTools.AddressGenerateResult from = AddressTools.generateShortAddress(Network.TESTNET);
      AddressTools.AddressGenerateResult to = AddressTools.generateShortAddress(Network.TESTNET);

      SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
      builder.addFrom(from.address);
      builder.addTo(new ToInfo(to.address, AmountUtils.ckbToShannon(100)));
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
      AddressTools.AddressGenerateResult from = AddressTools.generateShortAddress(Network.TESTNET);

      SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
      builder.addFrom(from.address);
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
    builder.addTo(new ToInfo(AddressWithKeyHolder.testAddress4(), new BigInteger("20")));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    System.out.println(new Gson().toJson(builder.build()));

    try {

      TransactionCompletionResponse transactionCompletionResponse =
          ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

      System.out.println(new Gson().toJson(transactionCompletionResponse));

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      String hash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSourceByChequeCell() {

    try {
      AddressTools.AddressGenerateResult to = AddressTools.generateShortAddress(Network.TESTNET);

      SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
      builder.addFrom(AddressWithKeyHolder.testAddress2());
      builder.addTo(new ToInfo(to.address, new BigInteger("20")));
      builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

      System.out.println(new Gson().toJson(builder.build()));

      TransactionCompletionResponse transactionCompletionResponse =
          ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      String hash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSourceByFree() {

    SimpleTransferPayloadBuilder builder = new SimpleTransferPayloadBuilder();
    builder.addFrom(AddressWithKeyHolder.testAddress4());
    builder.addTo(new ToInfo(AddressWithKeyHolder.testAddress1(), new BigInteger("20")));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse transactionCompletionResponse =
          ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      String hash = ApiFactory.getApi().sendTransaction(tx);
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
            AddressTools.generateAcpAddress(AddressWithKeyHolder.testAddress4()),
            new BigInteger("20")));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse transactionCompletionResponse =
          ApiFactory.getApi().buildSimpleTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      String hash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
