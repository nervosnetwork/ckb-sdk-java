package org.nervos.api.mercury;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import constant.UdtHolder;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.type.transaction.TransactionWithStatus;
import org.nervos.mercury.GsonFactory;
import org.nervos.mercury.model.GetBalancePayloadBuilder;
import org.nervos.mercury.model.TransferPayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.From;
import org.nervos.mercury.model.req.Mode;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.GetBalanceResponse;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

public class SourceTest {
  private String senderAddress = AddressWithKeyHolder.testAddress1();
  private String chequeCellReceiverAddress = AddressWithKeyHolder.testAddress2();
  private String receiverAddress = AddressWithKeyHolder.testAddress3();
  private String udtHash = "0xf21e7350fa9518ed3cbb008e0e8c941d7e01a12181931d5608aa366ee22228bd";
  private Gson g = GsonFactory.newGson();

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
      builder.item(ItemFactory.newIdentityItemByCkb(AddressWithKeyHolder.getPubKeyByAddress(addr)));
      builder.addAssetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));

      return ApiFactory.getApi().getBalance(builder.build());

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void issuingChequeCell() {
    TransferPayloadBuilder builder = new TransferPayloadBuilder();
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newIdentityItemByAddress(senderAddress)), Source.Free));

    builder.to(
        To.newTo(
            Arrays.asList(new ToInfo(chequeCellReceiverAddress, new BigInteger("100"))),
            Mode.HoldByFrom));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);

      byte[] hash = ApiFactory.getApi().sendTransaction(tx);

      while (ApiFactory.getApi().getTransaction(hash).txStatus.status == TransactionWithStatus.Status.PENDING) {
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
    builder.assetInfo(AssetInfo.newUdtAsset(UdtHolder.UDT_HASH));
    builder.from(
        From.newFrom(
            Arrays.asList(ItemFactory.newIdentityItemByAddress(chequeCellReceiverAddress)),
            Source.Claimable));

    builder.to(
        To.newTo(
            Arrays.asList(new ToInfo(receiverAddress, new BigInteger("100"))), Mode.HoldByFrom));

    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildTransferTransaction(builder.build());
      Transaction tx = sign(s);

      byte[] hash = ApiFactory.getApi().sendTransaction(tx);

      while (ApiFactory.getApi().getTransaction(hash).txStatus.status == TransactionWithStatus.Status.PENDING) {
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
    Transaction tx = SignUtils.sign(s);
    return tx;
  }
}
