package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.QueryTransactionsPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.Range;
import org.nervos.mercury.model.req.item.Address;
import org.nervos.mercury.model.resp.TransactionView;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsTest {

  Gson g = new Gson();

  @Test
  void testQueryGenericTransactionsWithCkb() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));

      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(getChequeAddress()));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(getAcpAddress()));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      builder.range(new Range(new BigInteger("2224987"), null));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      builder.range(new Range(null, new BigInteger("2224987")));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      builder.range(new Range(new BigInteger("2224993"), new BigInteger("2225023")));

      System.out.println(new Gson().toJson(builder.build()));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      // default limit 50
      builder.limit(new BigInteger("2"));

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      // default order desc
      builder.order("asc");

      PaginationResponse<TransactionView> resp =
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
      builder.item(new Address(AddressWithKeyHolder.queryTransactionAddress()));
      builder.limit(new BigInteger("1"));

      PaginationResponse<TransactionView> resp =
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
