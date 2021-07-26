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
import model.Action;
import model.FromKeyAddresses;
import model.Source;
import model.ToKeyAddress;
import model.TransferPayloadBuilder;
import model.resp.MercuryScriptGroup;
import model.resp.TransactionCompletionResponse;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.transaction.Transaction;

public class FeeRateTest {
  Gson g = new Gson();

  @Test
  void defaultFeeRate() {

    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress0())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress4(), Action.pay_by_from),
        new BigInteger("100")); // unit: CKB, 1 CKB = 10^8 Shannon
    // default 1000 shannons/KB
    //    builder.feeRate(new BigInteger("1000"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      System.out.println(g.toJson(s));
      List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
      Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.txView);

      for (MercuryScriptGroup sg : scriptGroups) {
        signBuilder.sign(sg, AddressWithKeyHolder.getKey(sg.pubKey));
      }

      Transaction tx = signBuilder.buildTx();

      String result = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void customizedFeeRate() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.from(
        new FromKeyAddresses(
            new HashSet<>(Arrays.asList(AddressWithKeyHolder.testAddress0())),
            Source.unconstrained));
    builder.addItem(
        new ToKeyAddress(AddressWithKeyHolder.testAddress4(), Action.pay_by_from),
        new BigInteger("100")); // unit: CKB, 1 CKB = 10^8 Shannon
    builder.feeRate(new BigInteger("10000"));

    try {
      TransactionCompletionResponse s =
          MercuryApiFactory.getApi().buildTransferTransaction(builder.build());
      System.out.println(g.toJson(s));
      List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
      Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.txView);

      for (MercuryScriptGroup sg : scriptGroups) {
        signBuilder.sign(sg, AddressWithKeyHolder.getKey(sg.pubKey));
      }

      Transaction tx = signBuilder.buildTx();

      String result = CkbNodeFactory.getApi().sendTransaction(tx);
      System.out.println(result);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
