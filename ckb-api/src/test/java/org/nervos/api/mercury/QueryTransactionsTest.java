package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.QueryTransactionsPayloadBuilder;
import org.nervos.mercury.model.common.*;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionInfoResponse;
import org.nervos.mercury.model.resp.TransactionWithRichStatus;
import org.nervos.mercury.model.resp.TxView;

import java.io.IOException;
import java.util.Objects;

public class QueryTransactionsTest {

  @Test
  void testQueryTransactionsWithCkb() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

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

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithAll() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

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

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

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

      PaginationResponse<TxView<TransactionInfoResponse>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionInfo(builder.build());

      System.out.println(resp.response.size());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsCellbase() {

    try {

      String minerAddress =
          "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqw6vjzy9kahx3lyvlgap8dp8ewd8g80pcgcexzrj";
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(minerAddress));
      builder.addAssetInfo(AssetInfo.newCkbAsset());
      builder.extraFilter(ExtraFilterType.CELL_BASE);
      builder.limit(3);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

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
      builder.extraFilter(ExtraFilterType.DAO);
      builder.limit(10);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

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
      builder.range(new Range(2778110, 2778201));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithLimit() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.limit(2);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

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
      builder.order(PaginationRequest.Order.ASC);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithTotalCount() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.limit(1);
      builder.returnCount(true);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithPage1() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.limit(1);
      builder.returnCount(true);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      if (Objects.isNull(resp.nextCursor)) {
        return;
      }

      while (Objects.nonNull(resp.nextCursor)) {
        QueryTransactionsPayloadBuilder builder2 = new QueryTransactionsPayloadBuilder();
        builder2.item(
            ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
        builder2.limit(1);
        builder2.returnCount(true);
        System.out.println(resp.nextCursor);
        builder2.cursor(resp.nextCursor);

        resp = ApiFactory.getApi().queryTransactionsWithTransactionView(builder2.build());
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
      builder.limit(1);
      builder.returnCount(true);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      if (Objects.isNull(resp.nextCursor)) {
        return;
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
