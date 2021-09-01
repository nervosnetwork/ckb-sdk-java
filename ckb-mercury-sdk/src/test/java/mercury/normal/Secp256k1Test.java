package mercury.normal;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import mercury.SignUtils;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import mercury.constant.UdtHolder;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.req.*;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

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
        AmountUtils.ckbToShannon(100));

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
        new ToNormalAddress(AddressWithKeyHolder.testAddress4()), AmountUtils.ckbToShannon(100));

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
