package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.*;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModeTest {
  @Test
  void transferCompletionCkbWithFree() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newCkbAsset());
    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress0())),
            Source.FREE));

    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress4(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM)); // unit: CKB, 1 CKB = 10^8 Shannon

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildTransferTransaction(builder.build());

      Transaction tx = sign(s);

      byte[] result = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void transferCompletionSudtWithFree() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress1())),
            Source.FREE));
    builder.to(
        To.newTo(
            Arrays.asList(new ToInfo(AddressWithKeyHolder.testAddress2(), 100)),
            Mode.HOLD_BY_FROM));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildTransferTransaction(builder.build());

      Transaction tx = sign(s);

      byte[] result = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void transferCompletionCkbWithClaimable() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newCkbAsset());
    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress1())),
            Source.CLAIMABLE));

    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress2(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM)); // unit: CKB, 1 CKB = 10^8 Shannon

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildTransferTransaction(builder.build());
    } catch (Exception e) {
      assertEquals("The transaction does not support ckb", e.getMessage());
    }
  }

  @Test
  void transferCompletionSudtWithClaimable() {
    // link SourceTest
  }

  @Test
  void transferCompletionSudtWithHoldByTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newCkbAsset());
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress1())),
            Source.FREE));
    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(
                    AddressTools.generateAcpAddress(AddressWithKeyHolder.testAddress4()),
                    100)),
            Mode.HOLD_BY_TO));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);

      byte[] result = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Transaction sign(TransactionCompletionResponse s) throws IOException {
    Transaction tx = SignUtils.sign(s);
    return tx;
  }
}
