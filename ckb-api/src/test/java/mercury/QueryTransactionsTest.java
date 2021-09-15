package mercury;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.QueryTransactionsPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.req.item.Address;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.resp.TransactionInfo;
import org.nervos.mercury.model.resp.TransactionView;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsTest {

  Gson g = new Gson();

  @Test
  void testQueryTransactionsWithCkb() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(new Gson().toJson(builder.build()));

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
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(new Gson().toJson(builder.build()));

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
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionInfo> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionInfo(builder.build());

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
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
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
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));

      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithChequeAddress() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(Item.newAddressItem(getChequeAddress()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithAcpAddress() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(Item.newAddressItem(getAcpAddress()));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithFromBlock() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.range(new Range(new BigInteger("2224987"), null));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithToBlock() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.range(new Range(null, new BigInteger("2224987")));

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
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.queryTransactionPubKey()));
      builder.range(new Range(new BigInteger("2224993"), new BigInteger("2225023")));

      System.out.println(new Gson().toJson(builder.build()));

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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      // default limit 50
      builder.limit(new BigInteger("2"));

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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
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
  void testQueryTransactionsWithOffset() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      builder.limit(new BigInteger("1"));

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

      System.out.println(resp.response.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithCount() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      builder.returnCount(true);

      PaginationResponse<TransactionView> resp =
          ApiFactory.getApi().queryTransactionsWithTransactionView(builder.build());

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
