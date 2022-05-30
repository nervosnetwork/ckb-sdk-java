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
    builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
    builder.addAssetInfo(AssetInfo.newCkbAsset());
    builder.limit(2);
    builder.order(PaginationRequest.Order.ASC);
    builder.range(new Range(2778100L, 3636218L));
    PaginationResponse<TransactionWithRichStatus> resp1 =
        ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());
    Assertions.assertEquals(2, resp1.response.size());
    Assertions.assertNotNull(resp1.response.get(0).value.transaction);
    Assertions.assertNotNull(resp1.response.get(0).value.txStatus);
    Assertions.assertNotNull(resp1.response.get(0).type);
    PaginationResponse<TransactionInfoResponse> resp2 =
        ApiFactory.getApi().queryTransactionsWithTransactionInfo(builder.build());
    Assertions.assertEquals(2, resp2.response.size());
    Assertions.assertNotNull(resp2.response.get(0).value.burn);
    Assertions.assertNotNull(resp2.response.get(0).value.records);
    Assertions.assertNotNull(resp2.response.get(0).type);
  }

  @Test
  void testQueryTransactionsWithPage() throws IOException {
    QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
    builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
    builder.limit(1);
    builder.returnCount(true);
    PaginationResponse<TransactionWithRichStatus> resp =
        ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());
    Assertions.assertEquals(1, resp.response.size());

    QueryTransactionsPayloadBuilder builder2 = new QueryTransactionsPayloadBuilder();
    builder2.item(
        ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
    builder2.limit(2);
    builder2.returnCount(true);
    builder2.cursor(resp.nextCursor);
    resp = ApiFactory.getApi().queryTransactionsWithTransactionView(builder2.build());
    Assertions.assertEquals(2, resp.response.size());
  }
}
