package mercury;

import java.io.IOException;
import java.util.List;
import mercury.constant.AddressWithKeyHolder;
import org.nervos.ckb.transaction.Secp256k1SighashAllBuilder;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.resp.MercuryScriptGroup;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

/** @author zjh @Created Date: 2021/7/23 @Description: @Modify by: */
public class SignUtils {
  public static Transaction sign(TransactionCompletionResponse s) throws IOException {
    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.txView);

    for (MercuryScriptGroup sg : scriptGroups) {
      signBuilder.sign(sg, AddressWithKeyHolder.getKey(sg.pubKey));
    }

    Transaction tx = signBuilder.buildTx();
    return tx;
  }

  public static Transaction signByKey(TransactionCompletionResponse s, String key)
      throws IOException {
    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    Secp256k1SighashAllBuilder signBuilder = new Secp256k1SighashAllBuilder(s.txView);

    for (MercuryScriptGroup sg : scriptGroups) {
      signBuilder.sign(sg, key);
    }

    Transaction tx = signBuilder.buildTx();
    return tx;
  }
}
