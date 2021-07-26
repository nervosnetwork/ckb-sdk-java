package mercury.normal;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import mercury.SignUtils;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import model.Action;
import model.FromKeyAddresses;
import model.FromNormalAddresses;
import model.Source;
import model.ToKeyAddress;
import model.ToNormalAddress;
import model.TransferPayloadBuilder;
import model.resp.TransactionCompletionResponse;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;

/** @author zjh @Created Date: 2021/7/23 @Description: @Modify by: */
public class Secp256k1Test {
  @Test
  void testFromSecp256k1() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.from(
        new FromNormalAddresses(new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress1()))));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress2(), Action.pay_by_from),
        new BigInteger("100"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      System.out.println(new Gson().toJson(s));
      Transaction tx = SignUtils.sign(s);

      String result = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testToSecp256k1() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.udtHash(UdtHolder.UDT_HASH);
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress0())),
            Source.unconstrained));
    builder.addItem(
        new ToNormalAddress(AddressWithKeyHolder.testAddress4()), new BigInteger("100"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      System.out.println(new Gson().toJson(s));
      Transaction tx = SignUtils.sign(s);

      String result = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
