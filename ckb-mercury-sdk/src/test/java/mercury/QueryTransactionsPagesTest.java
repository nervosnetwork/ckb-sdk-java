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
import org.nervos.mercury.model.req.KeyAddress;
import org.nervos.mercury.model.req.NormalAddress;
import org.nervos.mercury.model.resp.QueryTransactionsResponse;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryTransactionsPagesTest {

  Gson g = new Gson();

  @Test
  void testQueryTransactionsWithCkb() {

    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithUdt() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.addUdtHash(UdtHolder.UDT_HASH);

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithAll() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithChequeAddress() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new NormalAddress(getChequeAddress()));
      builder.allTransactionType();

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithAcpAddress() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new NormalAddress(getAcpAddress()));
      builder.allTransactionType();

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithFromBlock() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      builder.fromBlock(new BigInteger("2224987"));

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithToBlock() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      builder.toBlock(new BigInteger("2224987"));

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithFromBlockAndToBlock() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      builder.fromBlock(new BigInteger("2224993"));
      builder.toBlock(new BigInteger("2225023"));

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithLimit() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      // default limit 50
      builder.limit(new BigInteger("2"));

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithOrder() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      // default order desc
      builder.order("asc");

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testQueryTransactionsWithOffset() {
    try {
      QueryTransactionsPayloadBuilder builder = new QueryTransactionsPayloadBuilder();
      builder.address(new KeyAddress(AddressWithKeyHolder.queryTransactionAddress()));
      builder.allTransactionType();
      builder.limit(new BigInteger("1"));
      builder.offset(new BigInteger("1"));

      QueryTransactionsResponse resp =
          MercuryApiFactory.getApi().queryTransactions(builder.build());

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
