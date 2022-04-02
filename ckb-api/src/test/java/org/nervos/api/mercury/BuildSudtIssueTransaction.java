package org.nervos.api.mercury;

import com.google.gson.Gson;
import constant.AddressWithKeyHolder;
import constant.ApiFactory;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.AmountUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.mercury.GsonFactory;
import org.nervos.mercury.model.AdjustAccountPayloadBuilder;
import org.nervos.mercury.model.SudtIssuePayloadBuilder;
import org.nervos.mercury.model.common.AssetInfo;
import org.nervos.mercury.model.req.Mode;
import org.nervos.mercury.model.req.To;
import org.nervos.mercury.model.req.ToInfo;
import org.nervos.mercury.model.req.item.ItemFactory;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import utils.SignUtils;

public class BuildSudtIssueTransaction {
  Gson g = GsonFactory.newGson();

  @Test
  void testIssueSudt() {
    byte[] secp_code_hash = Numeric.hexStringToByteArray( "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8");
    byte[] sudt_testnet_code_hash = Numeric.hexStringToByteArray("0xc5e5dcf215925f7ef4dfaf5f4b4f105bc321c02776d6e7d52a1db3fcd9d011a4");
    String admin_address = AddressWithKeyHolder.testAddress0();
    byte[] admin_public_key = AddressWithKeyHolder.testPubKey0();
    String receiver_address = AddressWithKeyHolder.testAddress0();
    long issue_udt_amount = 1000000000;

    // 1. get admin lock hash
//    TestUtils.creteScript
    Script admin_script = new Script(secp_code_hash, admin_public_key, Script.HashType.TYPE);
    byte[] admin_lock_hash = admin_script.computeHash();
    System.out.println("admin_lock_hash: " + admin_lock_hash);

    // 2. get UDT hash
    Script udt_script = new Script(sudt_testnet_code_hash, admin_lock_hash, Script.HashType.TYPE);
    byte[] udt_hash = udt_script.computeHash();
    System.out.println("udt_hash: " + udt_hash);

    // 3. if this sudt is new for the chain, should do this step to upload udt script on chain
    // first.
    SudtIssuePayloadBuilder new_sudt_builder = new SudtIssuePayloadBuilder();
    new_sudt_builder.owner(admin_address);
    new_sudt_builder.to(
        To.newTo(
            Arrays.asList(new ToInfo(admin_address, AmountUtils.ckbToShannon(issue_udt_amount))),
            Mode.HOLD_BY_FROM));
    System.out.println(g.toJson(new_sudt_builder.build()));
    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildSudtIssueTransaction(new_sudt_builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);
      System.out.println(g.toJson(tx));
      byte[] txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println("upload udt cell: " + txHash);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 4. ensure receiver has one acp cell of this UDT
    AdjustAccountPayloadBuilder account_builder = new AdjustAccountPayloadBuilder();
    account_builder.item(ItemFactory.newAddressItem(receiver_address));
    account_builder.assetInfo(AssetInfo.newUdtAsset(udt_hash));
    account_builder.accountNumber(BigInteger.ONE);
    System.out.println(g.toJson(account_builder.build()));
    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildAdjustAccountTransaction(account_builder.build());

      if (!Objects.isNull(s)) {
        System.out.println(g.toJson(s));
        Transaction tx = SignUtils.sign(s);
        System.out.println(g.toJson(tx));
        byte[] txHash = ApiFactory.getApi().sendTransaction(tx);
        System.out.println("build acp cell: " + txHash);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 5. issue sUDT to receiver
    SudtIssuePayloadBuilder sudt_builder = new SudtIssuePayloadBuilder();
    sudt_builder.owner(admin_address);
    sudt_builder.to(
        To.newTo(
            Arrays.asList(new ToInfo(receiver_address, AmountUtils.ckbToShannon(issue_udt_amount))),
            Mode.HOLD_BY_TO));
    System.out.println(g.toJson(sudt_builder.build()));
    try {
      TransactionCompletionResponse s =
          ApiFactory.getApi().buildSudtIssueTransaction(sudt_builder.build());
      System.out.println(g.toJson(s));
      Transaction tx = SignUtils.sign(s);
      System.out.println(g.toJson(tx));
      byte[] txHash = ApiFactory.getApi().sendTransaction(tx);
      System.out.println("issue udt cell: " + txHash);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
