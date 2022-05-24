package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.mercury.model.DaoClaimPayloadBuilder;
import org.nervos.mercury.model.DaoDepositPayloadBuilder;
import org.nervos.mercury.model.DaoWithdrawPayloadBuilder;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

import java.io.IOException;
import java.util.Arrays;

public class DaoTest {

  @Test
  public void testDepositWithAddress() {
    DaoDepositPayloadBuilder builder = new DaoDepositPayloadBuilder();
    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3())),
            Source.FREE));
    builder.amount(AmountUtils.ckbToShannon(300));

    TransactionCompletionResponse transactionCompletionResponse = null;
    try {
      transactionCompletionResponse =
          ApiFactory.getApi().buildDaoDepositTransaction(builder.build());

      Transaction signTx = SignUtils.sign(transactionCompletionResponse);

      byte[] txHash = ApiFactory.getApi().sendTransaction(signTx);

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
            Source.FREE));

    builder.amount(AmountUtils.ckbToShannon(200));

    TransactionCompletionResponse transactionCompletionResponse = null;
    try {
      transactionCompletionResponse =
          ApiFactory.getApi().buildDaoDepositTransaction(builder.build());

      Transaction signTx = SignUtils.sign(transactionCompletionResponse);

      byte[] txHash = ApiFactory.getApi().sendTransaction(signTx);

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
            Source.FREE));
    builder.amount(AmountUtils.ckbToShannon(300));

    TransactionCompletionResponse transactionCompletionResponse = null;
    try {
      transactionCompletionResponse =
          ApiFactory.getApi().buildDaoDepositTransaction(builder.build());
      Transaction signTx = SignUtils.sign(transactionCompletionResponse);

      byte[] txHash = ApiFactory.getApi().sendTransaction(signTx);

      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testBuildDaoWithdraw() throws IOException {
    DaoWithdrawPayloadBuilder builder = new DaoWithdrawPayloadBuilder();
    builder.setFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.setPayFee(AddressWithKeyHolder.testAddress1());
    builder.setFeeRate(1200L);

    TransactionCompletionResponse tx =
        ApiFactory.getApi().buildDaoWithdrawTransaction(builder.build());
    Assertions.assertNotNull(tx.txView);
    Assertions.assertNotNull(tx.scriptGroups);
  }

  @Test
  public void testBuildDaoClaim() throws IOException {
    DaoClaimPayloadBuilder builder = new DaoClaimPayloadBuilder();
    builder.setFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    TransactionCompletionResponse tx =
        ApiFactory.getApi().buildDaoClaimTransaction(builder.build());
    Assertions.assertNotNull(tx.txView);
    Assertions.assertNotNull(tx.scriptGroups);
  }
}
