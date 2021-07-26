package mercury;

import com.google.common.primitives.Bytes;
import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import model.QueryGenericTransactionsPayloadBuilder;
import model.resp.QueryGenericTransactionsResponse;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressGenerator;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** @author zjh @Created Date: 2021/7/26 @Description: @Modify by: */
public class QueryGenericTransactionsPagesTest {

  Gson g = new Gson();

  @Test
  void TestQueryGenericTransactionsWithCkb() {

    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());

      QueryGenericTransactionsResponse resp =
          MercuryApiFactory.getApi().queryGenericTransactions(builder.build());

      System.out.println(resp.txs.size());
      System.out.println(g.toJson(resp));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void TestQueryGenericTransactionsWithUdt() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());
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
  void TestQueryGenericTransactionsWithAll() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());
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
  void TestQueryGenericTransactionsWithChequeAddress() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(getChequeAddress());
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
  void TestQueryGenericTransactionsWithAcpAddress() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(getAcpAddress());
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
  void TestQueryGenericTransactionsWithFromBlock() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());
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
  void TestQueryGenericTransactionsWithToBlock() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());
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
  void TestQueryGenericTransactionsWithFromBlockAndToBlock() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());
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
  void TestQueryGenericTransactionsWithLimit() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());
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
  void TestQueryGenericTransactionsWithOrder() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());
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
  void TestQueryGenericTransactionsWithOffset() {
    try {
      QueryGenericTransactionsPayloadBuilder builder = new QueryGenericTransactionsPayloadBuilder();
      builder.address(AddressWithKeyHolder.queryTransactionAddress());
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
    String pubKey = "0x8d135c59240be2229b19eec0be5a006b34b3b0cb";

    return AddressGenerator.generate(
        Network.TESTNET,
        new Script(
            "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
            pubKey,
            Script.TYPE));
  }

  private String getChequeAddress() {
    AddressParseResult senderScript = AddressParser.parse(AddressWithKeyHolder.testAddress0());
    AddressParseResult receiverScript =
        AddressParser.parse(AddressWithKeyHolder.queryTransactionAddress());

    System.out.println(senderScript.script.computeHash());
    System.out.println(receiverScript.script.computeHash());

    byte[] bytes =
        Bytes.concat(
            Numeric.hexStringToByteArray(
                Numeric.cleanHexPrefix(receiverScript.script.computeHash()).substring(0, 40)),
            Numeric.hexStringToByteArray(
                Numeric.cleanHexPrefix(senderScript.script.computeHash()).substring(0, 40)));

    String pubKey = Numeric.toHexStringNoPrefix(bytes);
    System.out.println(pubKey);

    String fullAddress =
        AddressGenerator.generate(
            Network.TESTNET,
            new Script(
                "0x60d5f39efce409c587cb9ea359cefdead650ca128f0bd9cb3855348f98c70d5b",
                pubKey,
                "type"));

    System.out.println(fullAddress);

    return fullAddress;
  }
}
