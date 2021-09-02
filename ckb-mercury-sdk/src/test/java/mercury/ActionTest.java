package mercury;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.req.Action;
import org.nervos.mercury.model.req.FromKeyAddresses;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.ToKeyAddress;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public class ActionTest {

  Gson g = new Gson();

  @Test
  void transferCompletionCkbWithPayByFrom() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();

    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress0())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress4(), Action.pay_by_from),
        AmountUtils.ckbToShannon(100)); // unit: CKB, 1 CKB = 10^8 Shannon

    System.out.println(new Gson().toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = sign(s);

      String result = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void transferCompletionSudtWithPayByFrom() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress1())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress2(), Action.pay_by_from),
        new BigInteger("100"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);

      String result = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void transferCompletionCkbWithLendByFrom() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress1())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress2(), Action.lend_by_from),
        AmountUtils.ckbToShannon(100));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
    } catch (Exception e) {
      assertEquals("The transaction does not support ckb", e.getMessage());
    }
  }

  @Test
  void transferCompletionSudtWithLendByFrom() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress1())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress2(), Action.lend_by_from),
        new BigInteger("100"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);
      System.out.println(g.toJson(s.txView));

      String result = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void transferCompletionCkbWithPayByTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress1())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress1(), Action.pay_by_to),
        AmountUtils.ckbToShannon(100));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
    } catch (Exception e) {
      assertEquals("The transaction does not support ckb", e.getMessage());
    }
  }

  @Test
  void transferCompletionSudtWithPayByTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress1())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress4(), Action.pay_by_to),
        new BigInteger("100"));

    System.out.println(g.toJson(builder.build()));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);
      System.out.println(g.toJson(s.txView));

      String result = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Transaction sign(TransactionCompletionResponse s) throws IOException {
    Transaction tx = SignUtils.sign(s);
    return tx;
  }
}
