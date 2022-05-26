package mercury;

import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.mercury.model.QueryTransactionsPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.PaginationRequest;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionInfoResponse;
import org.nervos.mercury.model.resp.TransactionWithRichStatus;

import java.io.IOException;

public class QueryTransactionsTest {
  @Test
  void testQueryTransactionsView() throws IOException {
    QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
    builder.setItem(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
    builder.addAssetInfo(AssetInfo.newCkbAsset());
    builder.setLimit(2);
    builder.setOrder(PaginationRequest.Order.ASC);
    builder.setRange(new Range(2778100L, 3636218L));
    PaginationResponse<TransactionWithRichStatus> resp1 =
        ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());
    Assertions.assertEquals(2, resp1.response.size());
    PaginationResponse<TransactionInfoResponse> resp2 =
        ApiFactory.getApi().queryTransactionsWithTransactionInfo(builder.build());
    Assertions.assertEquals(2, resp2.response.size());
  }

  @Test
  void testQueryTransactionsWithPage() throws IOException {
    QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
    builder.setItem(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
    builder.setLimit(1);
    builder.setReturnCount(true);
    PaginationResponse<TransactionWithRichStatus> resp =
        ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());
    Assertions.assertEquals(1, resp.response.size());

    QueryTransactionsPayloadBuilder builder2 = new QueryTransactionsPayloadBuilder();
    builder2.setItem(
        ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
    builder2.setLimit(2);
    builder2.setReturnCount(true);
    builder2.setCursor(resp.nextCursor);
    resp = ApiFactory.getApi().queryTransactionsWithTransactionView(builder2.build());
    Assertions.assertEquals(2, resp.response.size());
  }
}
