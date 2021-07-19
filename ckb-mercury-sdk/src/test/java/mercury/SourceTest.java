package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import model.Action;
import model.FromAccount;
import model.Source;
import model.ToAccount;
import model.TransferPayloadBuilder;
import model.resp.MercuryScriptGroup;
import model.resp.TransactionCompletionResponse;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.transaction.Transaction;

public class SourceTest {
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
    //    GetBalanceResponse ckbBalanceA = MercuryApiFactory.getApi().getBalance(null,
    // senderAddress);
    //    GetBalanceResponse udtBalanceA = MercuryApiFactory.getApi().getBalance(udtHash,
    // senderAddress);
    //    System.out.println("sender ckb balance: " + g.toJson(ckbBalanceA));
    //    System.out.println("sender udt balance: " + g.toJson(udtBalanceA));
    //
    //    GetBalanceResponse ckbBalanceB =
    //        MercuryApiFactory.getApi().getBalance(null, chequeCellReceiverAddress);
    //    GetBalanceResponse udtBalanceB =
    //        MercuryApiFactory.getApi().getBalance(udtHash, chequeCellReceiverAddress);
    //    System.out.println("cheque cell receiver ckb balance: " + g.toJson(ckbBalanceB));
    //    System.out.println("cheque cell receiver udt balance: " + g.toJson(udtBalanceB));
  }

  private void issuingChequeCell() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(udtHash);
    builder.from(new FromAccount(Arrays.asList(senderAddress), Source.unconstrained));
    builder.addItem(
        new ToAccount(chequeCellReceiverAddress, Action.lend_by_from), new BigInteger("100"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);

      while (CkbNodeFactory.getApi().getTransaction(hash).txStatus.status == "pending") {
        System.out.println("Awaiting transaction results");
        TimeUnit.SECONDS.sleep(1);
      }
      TimeUnit.SECONDS.sleep(20);

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
    builder.from(new FromAccount(Arrays.asList(chequeCellReceiverAddress), Source.fleeting));
    builder.addItem(new ToAccount(receiverAddress, Action.pay_by_from), new BigInteger("99"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);

      String hash = CkbNodeFactory.getApi().sendTransaction(tx);

      while (CkbNodeFactory.getApi().getTransaction(hash).txStatus.status == "pending") {
        System.out.println("Awaiting transaction results");
        TimeUnit.SECONDS.sleep(1);
      }
      TimeUnit.SECONDS.sleep(20);

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
}
