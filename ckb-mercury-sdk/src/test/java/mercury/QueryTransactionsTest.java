package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.QueryTransactionsPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.PaginationRequest;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionWithRichStatus;
import org.nervos.mercury.model.resp.TxView;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsTest {

  Gson g = new Gson();

  @Test
  void testQueryGenericTransactionsWithCkb() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithUdt() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
      builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithAll() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));

      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithChequeAddress() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(getChequeAddress()));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithAcpAddress() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(getAcpAddress()));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithFromBlock() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
      builder.range(new Range(2224987, null));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithToBlock() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
      builder.range(new Range(null, 2224987));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithFromBlockAndToBlock() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
      builder.range(new Range(2224993, 2225023));

      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithLimit() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
      // default limit 50
      builder.limit(2);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithOrder() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
      // default order desc
      builder.order(PaginationRequest.Order.ASC);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithOffset() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(ItemFactory.newAddressItem(AddressWithKeyHolder.queryTransactionAddress()));
      builder.limit(1);

      PaginationResponse<TxView<TransactionWithRichStatus>> resp =
          MercuryApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getAcpAddress() {
    return AddressTools.generateAcpAddress(AddressWithKeyHolder.queryTransactionAddress());
  }

  private String getChequeAddress() {
    return AddressTools.generateChequeAddress(
        AddressWithKeyHolder.testAddress0(), AddressWithKeyHolder.queryTransactionAddress());
  }
}
