package mercury;

import com.google.common.primitives.Bytes;
import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressGenerator;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;
import org.nervos.mercury.model.CollectAssetPayloadBuilder;
import org.nervos.mercury.model.GetBalancePayloadBuilder;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.*;
import org.nervos.mercury.model.req.item.Item;
import org.nervos.mercury.model.resp.GetBalanceResponse;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public class BuildAssetCollectionTransactionTest {

  Gson g = new Gson();

  @Test
  void testFromKeyAddressAndToKeyAddressWithCkb() {

    sendTx();
    printCexCkbBalance();

    CollectAssetPayloadBuilder builder = new CollectAssetPayloadBuilder();

    builder.fromAddress(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.cexAddress())), Source.unconstrained));
    builder.to(
        new ToKeyAddress(
            AddressWithKeyHolder.testAddress2(),
            Action.pay_by_from)); // unit: CKB, 1 CKB = 10^8 Shannon

    builder.feePaidBy(AddressWithKeyHolder.testAddress4());

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetCollectionTransaction(builder.build());

      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);

      waitTx(hash);
      printCexCkbBalance();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testFromNormalAddressesWithCkb() {

    sendTx();
    printCexCkbBalance();

    CollectAssetPayloadBuilder builder = new CollectAssetPayloadBuilder();

    builder.fromAddress(
        new FromNormalAddresses(new HashSet<>(Arrays.asList(AddressWithKeyHolder.cexAddress()))));
    builder.to(
        new ToKeyAddress(
            AddressWithKeyHolder.testAddress2(),
            Action.pay_by_from)); // unit: CKB, 1 CKB = 10^8 Shannon

    builder.feePaidBy(AddressWithKeyHolder.testAddress4());

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetCollectionTransaction(builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);

      waitTx(hash);
      printCexCkbBalance();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testToNormalAddressWithCkb() {

    sendTx();
    printCexCkbBalance();

    CollectAssetPayloadBuilder builder = new CollectAssetPayloadBuilder();

    builder.fromAddress(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.cexAddress())), Source.unconstrained));
    builder.to(
        new ToNormalAddress(
            AddressWithKeyHolder.testAddress2())); // unit: CKB, 1 CKB = 10^8 Shannon

    builder.feePaidBy(AddressWithKeyHolder.testAddress4());

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetCollectionTransaction(builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);

      waitTx(hash);
      printCexCkbBalance();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testFromNormalAddressesAndToNormalAddressWithCkb() {

    sendTx();
    printCexCkbBalance();

    CollectAssetPayloadBuilder builder = new CollectAssetPayloadBuilder();

    builder.fromAddress(
        new FromNormalAddresses(new HashSet<>(Arrays.asList(AddressWithKeyHolder.cexAddress()))));
    builder.to(
        new ToNormalAddress(
            AddressWithKeyHolder.testAddress2())); // unit: CKB, 1 CKB = 10^8 Shannon

    builder.feePaidBy(AddressWithKeyHolder.testAddress4());

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetCollectionTransaction(builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);

      waitTx(hash);
      printCexCkbBalance();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testFromKeyAddressAndToKeyAddressWithUdt() {

    sendLendByFrom();
    printCexUdtBalance();

    CollectAssetPayloadBuilder builder = new CollectAssetPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.fromAddress(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.cexAddress())), Source.fleeting));
    builder.to(
        new ToKeyAddress(
            AddressWithKeyHolder.testAddress3(),
            Action.pay_by_to)); // unit: CKB, 1 CKB = 10^8 Shannon

    builder.feePaidBy(AddressWithKeyHolder.testAddress4());

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetCollectionTransaction(builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);

      waitTx(hash);
      printCexUdtBalance();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testFromNormalAddressesWithUdt() {

    sendLendByFrom();
    printCexUdtBalance();

    CollectAssetPayloadBuilder builder = new CollectAssetPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.fromAddress(new FromNormalAddresses(new HashSet<>(Arrays.asList(getChequeAddress()))));
    builder.to(
        new ToKeyAddress(
            AddressWithKeyHolder.testAddress3(),
            Action.pay_by_to)); // unit: CKB, 1 CKB = 10^8 Shannon

    builder.feePaidBy(AddressWithKeyHolder.testAddress4());

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetCollectionTransaction(builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);

      waitTx(hash);
      printCexUdtBalance();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testToNormalAddressWithUdt() {

    sendLendByFrom();
    printCexUdtBalance();

    CollectAssetPayloadBuilder builder = new CollectAssetPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.fromAddress(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.cexAddress())), Source.fleeting));
    builder.to(new ToNormalAddress(getAcpAddress())); // unit: CKB, 1 CKB = 10^8 Shannon

    builder.feePaidBy(AddressWithKeyHolder.testAddress4());

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetCollectionTransaction(builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);

      waitTx(hash);
      printCexUdtBalance();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testFromNormalAddressesAndToNormalAddressWithUdt() {

    sendLendByFrom();
    printCexUdtBalance();

    CollectAssetPayloadBuilder builder = new CollectAssetPayloadBuilder();

    builder.udtHash(UdtHolder.UDT_HASH);
    builder.fromAddress(new FromNormalAddresses(new HashSet<>(Arrays.asList(getChequeAddress()))));
    builder.to(new ToNormalAddress(getAcpAddress())); // unit: CKB, 1 CKB = 10^8 Shannon

    builder.feePaidBy(AddressWithKeyHolder.testAddress4());

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildAssetCollectionTransaction(builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(hash);

      waitTx(hash);
      printCexUdtBalance();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void sendLendByFrom() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress0())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.cexAddress(), Action.lend_by_from),
        new BigInteger("100"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());

      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);

      while (CkbNodeFactory.getApi().getTransaction(hash).txStatus.status == "pending") {
        System.out.println("Awaiting transaction results");
        TimeUnit.SECONDS.sleep(1);
      }
      TimeUnit.SECONDS.sleep(60);

      System.out.println("send hash of cheque cell transactions: " + hash);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private String getChequeAddress() {
    AddressParseResult senderScript = AddressParser.parse(AddressWithKeyHolder.testAddress0());
    AddressParseResult receiverScript = AddressParser.parse(AddressWithKeyHolder.cexAddress());

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

  private String getAcpAddress() {
    String pubKey = "0x839f1806e85b40c13d3c73866045476cc9a8c214";

    return AddressGenerator.generate(
        Network.TESTNET,
        new Script(
            "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
            pubKey,
            Script.TYPE));
  }

  private void printCexUdtBalance() {

    try {
      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.cexPubKey()));
      builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

      GetBalanceResponse balance = MercuryApiFactory.getApi().getBalance(builder.build());
      System.out.printf("cex ckb balance: %s\n", g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendTx() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();

    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress4())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.cexAddress(), Action.pay_by_from),
        AmountUtils.ckbToShannon(100)); // unit: CKB, 1 CKB = 10^8 Shannon

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = SignUtils.sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);

      waitTx(hash);

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void waitTx(String hash) throws IOException, InterruptedException {
    while (CkbNodeFactory.getApi().getTransaction(hash).txStatus.status == "pending") {
      System.out.println("Awaiting transaction results");
      TimeUnit.SECONDS.sleep(1);
    }
    TimeUnit.SECONDS.sleep(60);

    System.out.println("send hash of transactions: " + hash);
  }

  private void printCexCkbBalance() {

    try {
      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.item(Item.newIdentityItemByCkb(AddressWithKeyHolder.cexPubKey()));
      builder.addAssetInfo(AssetInfo.newCkbAsset());

      GetBalanceResponse balance = MercuryApiFactory.getApi().getBalance(builder.build());
      System.out.printf("cex ckb balance: %s\n", g.toJson(balance));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
