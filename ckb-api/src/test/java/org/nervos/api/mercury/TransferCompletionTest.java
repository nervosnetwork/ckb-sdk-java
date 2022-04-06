package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.Mode;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

public class TransferCompletionTest {

  @Test
  void testSingleFromSingleTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newCkbAsset());
    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress1())),
            Source.FREE));
    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress2(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM));

    try {
      sendTx(builder);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSingleFromMultiTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress1())),
            Source.FREE));

    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress2(), AmountUtils.ckbToShannon(100)),
                new ToInfo(AddressWithKeyHolder.testAddress3(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM));

    try {
      sendTx(builder);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testMultiFromSingleTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress1()),
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress2())),
            Source.FREE));

    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress3(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM));

    try {
      sendTx(builder);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testPayFee() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();

    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress1())),
            Source.FREE));

    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress2(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM));

    builder.payFee(AddressWithKeyHolder.testAddress3());

    try {
      sendTx(builder);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testChange() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();

    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress1())),
            Source.FREE));

    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress2(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM));

    builder.change(AddressWithKeyHolder.testAddress4());

    try {
      sendTx(builder);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testPWlock() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newAddressItem(AddressWithKeyHolder.PW_LOCK_ADDRESS)),
            Source.FREE));

    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress2(), AmountUtils.ckbToShannon(1))),
            Mode.HOLD_BY_TO));

    builder.change(AddressWithKeyHolder.testAddress4());

    try {
      sendTx(builder);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testPWlockUDT() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(
        AssetInfo.newUdtAsset(
            Numeric.hexStringToByteArray("0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd")));
    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newAddressItem(AddressWithKeyHolder.PW_LOCK_ADDRESS)),
            Source.FREE));

    builder.to(
        To.newTo(
            Arrays.asList(new ToInfo(AddressWithKeyHolder.testAddress2(), new BigInteger("1"))),
            Mode.HOLD_BY_TO));

    builder.change(AddressWithKeyHolder.testAddress4());

    try {
      sendTx(builder);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendTx(TransferPayloadBuilder builder) throws IOException {
    TransactionCompletionResponse s = ApiFactory.getApi().buildTransferTransaction(builder.build());

    Transaction tx = SignUtils.sign(s);

    byte[] txHash = ApiFactory.getApi().sendTransaction(tx);
    System.out.println(txHash);
  }
}
