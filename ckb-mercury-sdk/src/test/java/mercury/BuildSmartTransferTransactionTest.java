package mercury;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.SmartTransferPayloadBuilder;
import org.nervos.mercury.model.req.SmartTo;
import org.nervos.mercury.model.resp.AssetInfo;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public class BuildSmartTransferTransactionTest {

  @Test
  void testAccountNumber() {
    try {
      Integer accountNum =
          MercuryApiFactory.getApi().getAccountNumber(AddressWithKeyHolder.testAddress4());
      System.out.println(accountNum);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testCkbInsufficientBalanceToPayTheFee1() {
    try {
      AddressTools.AddressGenerateResult from = AddressTools.generateShortAddress(Network.TESTNET);
      AddressTools.AddressGenerateResult to = AddressTools.generateShortAddress(Network.TESTNET);

      SmartTransferPayloadBuilder builder = new SmartTransferPayloadBuilder();
      builder.addFrom(from.address);
      builder.addTo(new SmartTo(to.address, AmountUtils.ckbToShannon(100)));
      builder.assetInfo(AssetInfo.newCkbAsset());

    } catch (Exception e) {
      assertEquals("CKB Insufficient balance to pay the fee", e.getMessage());
    }
  }

  @Test
  void testCkbInsufficientBalanceToPayTheFee2() {
    try {
      AddressTools.AddressGenerateResult from = AddressTools.generateShortAddress(Network.TESTNET);

      SmartTransferPayloadBuilder builder = new SmartTransferPayloadBuilder();
      builder.addFrom(from.address);
      builder.addTo(
          new SmartTo(AddressWithKeyHolder.testAddress4(), AmountUtils.ckbToShannon(100)));
      builder.assetInfo(AssetInfo.newCkbAsset());

      MercuryApiFactory.getApi().buildSmartTransferTransaction(builder.build());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSourceByFleeting() {

    SmartTransferPayloadBuilder builder = new SmartTransferPayloadBuilder();
    builder.addFrom(AddressWithKeyHolder.testAddress2());
    builder.addTo(new SmartTo(AddressWithKeyHolder.testAddress4(), new BigInteger("20")));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    try {
      TransactionCompletionResponse transactionCompletionResponse =
          MercuryApiFactory.getApi().buildSmartTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSourceByUnconstrained() {

    SmartTransferPayloadBuilder builder = new SmartTransferPayloadBuilder();
    builder.addFrom(AddressWithKeyHolder.testAddress4());
    builder.addTo(new SmartTo(AddressWithKeyHolder.testAddress1(), new BigInteger("20")));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    try {
      TransactionCompletionResponse transactionCompletionResponse =
          MercuryApiFactory.getApi().buildSmartTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testActionByPayByTo() {

    SmartTransferPayloadBuilder builder = new SmartTransferPayloadBuilder();
    builder.addFrom(AddressWithKeyHolder.testAddress4());
    builder.addTo(new SmartTo(AddressWithKeyHolder.testAddress1(), new BigInteger("20")));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    try {
      TransactionCompletionResponse transactionCompletionResponse =
          MercuryApiFactory.getApi().buildSmartTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testActionByPayByFrom() {

    try {
      AddressTools.AddressGenerateResult to = AddressTools.generateShortAddress(Network.TESTNET);

      SmartTransferPayloadBuilder builder = new SmartTransferPayloadBuilder();
      builder.addFrom(AddressWithKeyHolder.testAddress4());
      builder.addTo(new SmartTo(to.address, new BigInteger("20")));
      builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
      TransactionCompletionResponse transactionCompletionResponse =
          MercuryApiFactory.getApi().buildSmartTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(transactionCompletionResponse);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
