package org.nervos.api.mercury;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.mercury.GsonFactory;
import org.nervos.mercury.model.DaoClaimPayloadBuilder;
import org.nervos.mercury.model.DaoDepositPayloadBuilder;
import org.nervos.mercury.model.DaoWithdrawPayloadBuilder;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

public class DaoTest {
  Gson g = GsonFactory.newGson();

  @Test
  public void testDepositWithAddress() {
    DaoDepositPayloadBuilder builder = new DaoDepositPayloadBuilder();
    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3())),
            Source.Free));
    builder.amount(AmountUtils.ckbToShannon(300));

    System.out.println(g.toJson(builder));

    TransactionCompletionResponse transactionCompletionResponse = null;
    try {
      transactionCompletionResponse =
          ApiFactory.getApi().buildDaoDepositTransaction(builder.build());
      System.out.println(g.toJson(transactionCompletionResponse));

      Transaction signTx = SignUtils.sign(transactionCompletionResponse);

      String txHash = ApiFactory.getApi().sendTransaction(signTx);

      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDepositWithPWLockAddress() {
    DaoDepositPayloadBuilder builder = new DaoDepositPayloadBuilder();

    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newAddressItem(AddressWithKeyHolder.PW_LOCK_ADDRESS)),
            Source.Free));

    builder.amount(AmountUtils.ckbToShannon(200));

    System.out.println(g.toJson(builder));

    TransactionCompletionResponse transactionCompletionResponse = null;
    try {
      transactionCompletionResponse =
          ApiFactory.getApi().buildDaoDepositTransaction(builder.build());
      System.out.println(g.toJson(transactionCompletionResponse));

      Transaction signTx = SignUtils.sign(transactionCompletionResponse);

      String txHash = ApiFactory.getApi().sendTransaction(signTx);

      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDepositWithIdentity() {
    DaoDepositPayloadBuilder builder = new DaoDepositPayloadBuilder();
    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey3())),
            Source.Free));
    builder.amount(AmountUtils.ckbToShannon(300));

    System.out.println(g.toJson(builder));

    TransactionCompletionResponse transactionCompletionResponse = null;
    try {
      transactionCompletionResponse =
          ApiFactory.getApi().buildDaoDepositTransaction(builder.build());
      Transaction signTx = SignUtils.sign(transactionCompletionResponse);

      String txHash = ApiFactory.getApi().sendTransaction(signTx);

      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithdraw() {
    DaoWithdrawPayloadBuilder builder = new DaoWithdrawPayloadBuilder();
    builder.from(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.payFee(AddressWithKeyHolder.testAddress1());

    System.out.println(g.toJson(builder));

    TransactionCompletionResponse transactionCompletionResponse = null;
    try {
      transactionCompletionResponse =
          ApiFactory.getApi().buildDaoWithdrawTransaction(builder.build());

      Transaction signTx = SignUtils.sign(transactionCompletionResponse);
      String txHash = ApiFactory.getApi().sendTransaction(signTx);

      System.out.println(txHash);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(g.toJson(transactionCompletionResponse));
  }

  @Test
  public void testClaim() {
    DaoClaimPayloadBuilder builder = new DaoClaimPayloadBuilder();
    builder.from(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));

    System.out.println(g.toJson(builder));

    TransactionCompletionResponse transactionCompletionResponse = null;
    try {
      transactionCompletionResponse = ApiFactory.getApi().buildDaoClaimTransaction(builder.build());
      System.out.println(g.toJson(transactionCompletionResponse));

      Transaction signTx = SignUtils.sign(transactionCompletionResponse);
      String txHash = ApiFactory.getApi().sendTransaction(signTx);

      System.out.println(txHash);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(g.toJson(transactionCompletionResponse));
  }
}
