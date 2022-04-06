package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
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

public class FeeRateTest {

  @Test
  void defaultFeeRate() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newCkbAsset());
    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress0())),
            Source.FREE));
    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress4(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM)); // unit: CKB, 1 CKB = 10^8 Shannon
    // default 1000 shannons/KB
    //    builder.feeRate(new BigInteger("1000"));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = SignUtils.sign(s);

      byte[] result = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void customizedFeeRate() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newCkbAsset());
    builder.from(
        From.newFrom(
            Arrays.asList(
                ItemFactory.newIdentityItemByAddress(AddressWithKeyHolder.testAddress0())),
            Source.FREE));
    builder.to(
        To.newTo(
            Arrays.asList(
                new ToInfo(AddressWithKeyHolder.testAddress4(), AmountUtils.ckbToShannon(100))),
            Mode.HOLD_BY_FROM)); // unit: CKB, 1 CKB = 10^8 Shannon
    builder.feeRate(new BigInteger("10000"));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(s);

      byte[] result = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
