package utils;

import constant.AddressWithKeyHolder;
import java.util.List;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.resp.MercuryScriptGroup;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import org.nervos.mercury.signature.Secp256k1SighashBuilder;

/** @author zjh @Created Date: 2021/7/23 @Description: @Modify by: */
public class SignUtils {
  public static Transaction sign(TransactionCompletionResponse s) {
    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    Secp256k1SighashBuilder signBuilder = new Secp256k1SighashBuilder(s.txView);

    for (MercuryScriptGroup sg : scriptGroups) {
      signBuilder.sign(sg, AddressWithKeyHolder.getKey(sg.getAddress()));
    }

    Transaction tx = signBuilder.buildTx();
    return tx;
  }

  public static Transaction signByKey(TransactionCompletionResponse s, String key) {
    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    Secp256k1SighashBuilder signBuilder = new Secp256k1SighashBuilder(s.txView);

    for (MercuryScriptGroup sg : scriptGroups) {
      signBuilder.sign(sg, key);
    }

    Transaction tx = signBuilder.buildTx();
    return tx;
  }
}
