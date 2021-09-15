package org.nervos.api.mercury;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.AdjustAccountPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

public class BuildAdjustAccountTest {

  Gson g = new Gson();

  @Test
  void testCreateAsset()
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

    AddressTools.AddressGenerateResult newAddress =
        AddressTools.generateShortAddress(Network.TESTNET);

    AddressWithKeyHolder.put(newAddress.address, newAddress.privateKey);

    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(Item.newIdentityItemByCkb(newAddress.lockArgs));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.addFrom(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey3()));
    builder.accountNumber(BigInteger.ONE);

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      Transaction tx = SignUtils.sign(s);

      System.out.println(g.toJson(tx));
      String txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testAdjustAssetAccountWithUdt() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.accountNumber(BigInteger.ONE);

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      System.out.println(new Gson().toJson(s));

      Transaction tx = SignUtils.sign(s);

      System.out.println(g.toJson(tx));
      String txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testAdjustAssetPayFrom() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.addFrom(Item.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.accountNumber(BigInteger.valueOf(1));

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      System.out.println(new Gson().toJson(s));

      Transaction tx = SignUtils.sign(s);

      System.out.println(g.toJson(tx));
      String txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testAdjustAssetExtraCkb() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey2()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.extraCkb(AmountUtils.ckbToShannon(200));
    builder.accountNumber(BigInteger.ONE);

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      System.out.println(new Gson().toJson(s));

      Transaction tx = SignUtils.sign(s);

      System.out.println(g.toJson(tx));
      String txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
