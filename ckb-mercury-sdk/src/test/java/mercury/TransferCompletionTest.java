package mercury;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import mercury.constant.AddressWithKeyHolder;
import mercury.constant.CkbNodeFactory;
import mercury.constant.MercuryApiFactory;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.req.Action;
import org.nervos.mercury.model.req.FromKeyAddresses;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.ToKeyAddress;
import org.nervos.mercury.model.resp.MercuryScriptGroup;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public class TransferCompletionTest {

  Gson g = new Gson();

  @Test
  void testSingleFromSingleTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress1())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress2(), Action.pay_by_from),
        new BigInteger("100"));

    try {
      sendTx(builder);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSingleFromMultiTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress1())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress2(), Action.pay_by_from),
        new BigInteger("100"));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress3(), Action.pay_by_from),
        new BigInteger("100"));

    try {
      sendTx(builder);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testMultiFromSingleTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(
                Arrays.asList(
                    AddressWithKeyHolder.testAddress1(), AddressWithKeyHolder.testAddress2())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress3(), Action.pay_by_from),
        new BigInteger("100"));

    System.out.println(g.toJson(builder.build()));

    try {
      sendTx(builder);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testMultiFromMultiTo() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(
                Arrays.asList(
                    AddressWithKeyHolder.testAddress1(), AddressWithKeyHolder.testAddress2())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress3(), Action.pay_by_from),
        new BigInteger("100"));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress4(), Action.pay_by_from),
        new BigInteger("100"));

    System.out.println(g.toJson(builder.build()));

    try {
      sendTx(builder);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendTx(TransferPayloadBuilder builder) throws IOException {
    TransactionCompletionResponse s =
        MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.txView);

    for (MercuryScriptGroup sg : scriptGroups) {
      signBuilder.sign(sg, AddressWithKeyHolder.getKey(sg.pubKey));
    }

    Transaction tx = signBuilder.buildTx();

    System.out.println(g.toJson(tx));
    String txHash = CkbNodeFactory.getApi().sendTransaction(tx);
    System.out.println(txHash);
  }
}
