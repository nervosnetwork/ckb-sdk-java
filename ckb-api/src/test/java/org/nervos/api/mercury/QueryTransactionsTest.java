package org.nervos.api.mercury;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.QueryTransactionsPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.ExtraFilterType;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionInfo;
import org.nervos.mercury.model.resp.TransactionView;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsTest {

  Gson g = new Gson();

  @Test
  void testQueryTransactionsWithCkb() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithUdt() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithAll() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsView() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsInfo() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionInfo> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionInfo(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsCellbase() {

    try {

      String minerAddress = "ckt1qyqd5eyygtdmwdr7ge736zw6z0ju6wsw7rssu8fcve";
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(minerAddress));
      builder.addAssetInfo(AssetInfo.newCkbAsset());
      builder.extraFilter(ExtraFilterType.CellBase);
      builder.limit(BigInteger.valueOf(3));

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsDao() {

    try {

      String daoAddress = AddressWithKeyHolder.testAddress3();
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(daoAddress));
      builder.addAssetInfo(AssetInfo.newCkbAsset());
      builder.extraFilter(ExtraFilterType.Dao);
      builder.limit(BigInteger.valueOf(10));

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithFromBlockAndToBlock() {
    try {

      Script script = AddressTools.parse(AddressWithKeyHolder.queryTransactionAddress()).script;
      System.out.println(script.computeHash());

      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.range(new Range(new BigInteger("2778110"), new BigInteger("2778201")));

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithLimit() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.limit(new BigInteger("2"));

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithOrder() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      // default order desc
      builder.order("asc");

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithTotalCount() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.limit(new BigInteger("1"));
      builder.returnCount(true);

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      //      System.out.println(new Gson().toJson(builder.build()));

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithPage1() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.limit(new BigInteger("1"));
      builder.returnCount(true);

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(new Gson().toJson(resp));

      if (Objects.isNull(resp.nextCursor)) {
        return;
      }

      while (Objects.nonNull(resp.nextCursor)) {
        QueryTransactionsPayloadBuilder builder2 = new QueryTransactionsPayloadBuilder();
        builder2.item(
            ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
        builder2.limit(new BigInteger("1"));
        builder2.returnCount(true);
        System.out.println(resp.nextCursor);
        builder2.cursor(resp.nextCursor);

        System.out.println(new Gson().toJson(builder2.build()));
        resp = ApiFactory.getApi().queryTransactionsWithTransactionView(builder2.build());

        System.out.println(new Gson().toJson(resp));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithPage2() {
    try {
      // total count = 3
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.limit(new BigInteger("1"));
      builder.pageNumber(BigInteger.valueOf(3));
      builder.returnCount(true);

      //      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(new Gson().toJson(resp));

      if (Objects.isNull(resp.nextCursor)) {
        return;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
