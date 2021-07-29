package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.address.AddressTools;
import org.nervos.mercury.model.QueryGenericTransactionsPayloadBuilder;
import org.nervos.mercury.model.req.KeyAddress;
import org.nervos.mercury.model.req.NormalAddress;
import org.nervos.mercury.model.resp.QueryGenericTransactionsResponse;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryGenericTransactionsPagesTest {

  Gson g = new Gson();

  @Test
  void testQueryGenericTransactionsWithCkb() {

    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithUdt() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.addUdtHash(UdtHolder.UDT_HASH);

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithAll() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithChequeAddress() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new NormalAddress(getChequeAddress()));
      builder.allTransactionType();

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithAcpAddress() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new NormalAddress(getAcpAddress()));
      builder.allTransactionType();

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithFromBlock() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      builder.fromBlock(new BigInteger("2224987"));

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithToBlock() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      builder.toBlock(new BigInteger("2224987"));

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithFromBlockAndToBlock() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      builder.fromBlock(new BigInteger("2224993"));
      builder.toBlock(new BigInteger("2225023"));

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithLimit() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      // default limit 50
      builder.limit(new BigInteger("2"));

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithOrder() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      // default order desc
      builder.order("asc");

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryGenericTransactionsWithOffset() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      builder.limit(new BigInteger("1"));
      builder.offset(new BigInteger("1"));

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
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
