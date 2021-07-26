package mercury.normal;

import com.google.common.primitives.Bytes;
import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import model.Action;
import model.FromKeyAddresses;
import model.FromNormalAddresses;
import model.GetBalancePayloadBuilder;
import model.Source;
import model.ToKeyAddress;
import model.TransferPayloadBuilder;
import model.resp.GetBalanceResponse;
import model.resp.MercuryScriptGroup;
import model.resp.TransactionCompletionResponse;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressGenerator;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

public class ChequeTest {
  private String senderAddress = AddressWithKeyHolder.testAddress1();
  private String chequeCellReceiverAddress = AddressWithKeyHolder.testAddress2();
  private String receiverAddress = AddressWithKeyHolder.testAddress3();
  private String udtHash = "0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd";
  private Gson g = new Gson();

  @Test
  void test() {

    try {

      printBalance();
      issuingChequeCell();
      printBalance();
      claimChequeCell();
      printBalance();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void printBalance() throws IOException {

    System.out.println("sender ckb balance: " + g.toJson(getBalance(senderAddress, null)));
    System.out.println("sender udt balance: " + g.toJson(getBalance(senderAddress, udtHash)));

    System.out.println(
        "cheque cell receiver ckb balance: "
            + g.toJson(getBalance(chequeCellReceiverAddress, null)));
    System.out.println(
        "cheque cell receiver udt balance: "
            + g.toJson(getBalance(chequeCellReceiverAddress, udtHash)));
  }

  private GetBalanceResponse getBalance(String addr, String udtHash) {
    try {

      GetBalancePayloadBuilder builder = new GetBalancePayloadBuilder();
      builder.address(addr);
      builder.addUdtHash(udtHash);

      return MercuryApiFactory.getApi().getBalance(builder.build());

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void issuingChequeCell() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(udtHash);
    builder.from(
        new FromKeyAddresses(new HashSet<>(Arrays.asList(senderAddress)), Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(chequeCellReceiverAddress, Action.lend_by_from), new BigInteger("100"));

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());

      System.out.println(new Gson().toJson(s));

      Transaction tx = sign(s);

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

  private void claimChequeCell() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(udtHash);
    builder.from(new FromNormalAddresses(new HashSet<>(Arrays.asList(getChequeAddress()))));
    builder.addItem(new ToKeyAddress(receiverAddress, Action.pay_by_from), new BigInteger("99"));

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);

      while (CkbNodeFactory.getApi().getTransaction(hash).txStatus.status == "pending") {
        System.out.println("Awaiting transaction results");
        TimeUnit.SECONDS.sleep(1);
      }
      TimeUnit.SECONDS.sleep(60);

      System.out.println("claim hash of cheque cell transactions: " + hash);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private Transaction sign(TransactionCompletionResponse s) throws IOException {
    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.txView);

    for (MercuryScriptGroup sg : scriptGroups) {
      signBuilder.sign(sg, AddressWithKeyHolder.getKey(sg.pubKey));
    }

    Transaction tx = signBuilder.buildTx();
    return tx;
  }

  //  @Test
  //  void test2() {
  //    String chequeAddress = getChequeAddress();
  //    AddressParseResult parse = AddressParser.parse(chequeAddress);
  //    System.out.println(Numeric.cleanHexPrefix(parse.script.computeHash()).substring(0, 40));
  //
  //    AddressParseResult parse2 = AddressParser.parse(senderAddress);
  //    System.out.println(Numeric.cleanHexPrefix(parse2.script.computeHash()).substring(0, 40));
  //  }

  private String getChequeAddress() {
    AddressParseResult parse1 = AddressParser.parse(senderAddress);
    System.out.println(new Gson().toJson(parse1));

    AddressParseResult parse2 = AddressParser.parse(chequeCellReceiverAddress);
    System.out.println(new Gson().toJson(parse2));

    System.out.println(parse1.script.computeHash());
    System.out.println(parse2.script.computeHash());

    byte[] bytes =
        Bytes.concat(
            Numeric.hexStringToByteArray(
                Numeric.cleanHexPrefix(parse2.script.computeHash()).substring(0, 40)),
            Numeric.hexStringToByteArray(
                Numeric.cleanHexPrefix(parse1.script.computeHash()).substring(0, 40)));

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
