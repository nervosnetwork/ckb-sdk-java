package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.AdjustAccountPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Objects;

public class BuildAdjustAccountTest {

  @Test
  void testCreateAsset()
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

    AddressTools.AddressGenerateResult newAddress =
        AddressTools.generateShortAddress(Network.TESTNET);

    AddressWithKeyHolder.put(newAddress.address, newAddress.privateKey);

    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(Numeric.toHexString(newAddress.lockArgs)));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.addFrom(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey3()));
    builder.accountNumber(BigInteger.ONE);

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      Transaction tx = SignUtils.sign(s);

      byte[] txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testAdjustAssetAccountWithUdt() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.accountNumber(BigInteger.ONE);

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      Transaction tx = SignUtils.sign(s);

      byte[] txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testAdjustAssetPayFrom() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.addFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.accountNumber(BigInteger.valueOf(1));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      Transaction tx = SignUtils.sign(s);

      byte[] txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testAdjustAssetExtraCkb() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey2()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.extraCkb(AmountUtils.ckbToShannon(200));
    builder.accountNumber(BigInteger.ONE);

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      Transaction tx = SignUtils.sign(s);
      byte[] txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
