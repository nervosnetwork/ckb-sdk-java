package org.nervos.api.mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.QueryTransactionsPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.PaginationRequest;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionWithRichStatus;

import java.io.IOException;

public class QueryTransactionsTest {
  @Test
  void testQueryGenericTransactionsWithStatus() throws IOException {
    QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
    builder.setItem(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
    builder.addAssetInfo(AssetInfo.newCkbAsset());
    builder.setLimit(2);
    builder.setOrder(PaginationRequest.Order.ASC);
    builder.setRange(new Range(2778100L, 3636218L));
    PaginationResponse<TransactionWithRichStatus> resp =
        ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());
    Assertions.assertTrue(resp.response.size() == 2);
  }

  @Test
  void testQueryTransactionsWithCkb() throws IOException {
    QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
    builder.setItem(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
    builder.addAssetInfo(AssetInfo.newCkbAsset());

    PaginationResponse<TransactionWithRichStatus> resp =
        ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());
  }

  @Test
  void testQueryTransactionsWithUdt() throws IOException {
    QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
    builder.setItem(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
    builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

    PaginationResponse<TransactionWithRichStatus> resp =
        ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());
  }

  @Test
  void testQueryTransactionsWithPage1() throws IOException {
    QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
    builder.setItem(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
    builder.setLimit(1);
    builder.setReturnCount(true);
    PaginationResponse<TransactionWithRichStatus> resp =
        ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

    QueryTransactionsPayloadBuilder builder2 = new QueryTransactionsPayloadBuilder();
    builder2.setItem(
        ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
    builder2.setLimit(1);
    builder2.setReturnCount(true);
    builder2.setCursor(resp.nextCursor);
    resp = ApiFactory.getApi().queryTransactionsWithTransactionView(builder2.build());
    Assertions.assertTrue(resp.response.size() != resp.count);
  }
}
