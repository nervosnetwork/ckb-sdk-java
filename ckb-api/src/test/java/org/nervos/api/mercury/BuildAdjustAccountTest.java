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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.GsonFactory;
import org.nervos.mercury.model.AdjustAccountPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

public class BuildAdjustAccountTest {
  Gson g = GsonFactory.newGson();

  @Test
  void testCreateAsset()
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

    AddressTools.AddressGenerateResult newAddress =
        AddressTools.generateShortAddress(Network.TESTNET);

    AddressWithKeyHolder.put(newAddress.address, newAddress.privateKey);

    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(newAddress.lockArgs));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.addFrom(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey3()));
    builder.accountNumber(BigInteger.ONE);

    System.out.println(g.toJson(builder.build()));

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
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.accountNumber(BigInteger.ONE);

    System.out.println(g.toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      System.out.println(g.toJson(s));

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
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey4()));
    builder.addFrom(ItemFactory.newAddressItem(AddressWithKeyHolder.testAddress3()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.accountNumber(BigInteger.valueOf(1));

    System.out.println(g.toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      System.out.println(g.toJson(s));

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
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.testPubKey2()));
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.extraCkb(AmountUtils.ckbToShannon(200));
    builder.accountNumber(BigInteger.ONE);

    System.out.println(g.toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }

      System.out.println(g.toJson(s));

      Transaction tx = SignUtils.sign(s);

      System.out.println(g.toJson(tx));
      String txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testPwlock() {
    AdjustAccountPayloadBuilder builder = new AdjustAccountPayloadBuilder();
    builder.item(
        ItemFactory.newAddressItem(
            "ckt1qpvvtay34wndv9nckl8hah6fzzcltcqwcrx79apwp2a5lkd07fdxxqdd40lmnsnukjh3qr88hjnfqvc4yg8g0gskp8ffv"));

    Set<Item> from = new HashSet();
    from.add(
        ItemFactory.newAddressItem(
            "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqthh5pum5pzqpssk47zk67hnd6lm28rnqs4cnj0w"));
    builder.from = from;

    builder.assetInfo(
        AssetInfo.newUdtAsset(
            "0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd"));
    builder.accountNumber(new BigInteger("5"));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(builder.build());

      if (Objects.isNull(s)) {
        return;
      }
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      System.out.println(g.toJson(tx));
      String txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println(txHash);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
