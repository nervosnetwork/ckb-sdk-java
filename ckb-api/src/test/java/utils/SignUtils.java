package utils;

import org.nervos.ckb.type.Transaction;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;

public class SignUtils {
  // TODO: fix all usages in examples
  public static Transaction sign(TransactionCompletionResponse s) {
    //    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    //    TransactionSigner signer = new TransactionSigner(s.txView);
    //
    //    for (MercuryScriptGroup sg : scriptGroups) {
    //      if (isPWEthereum(sg)) {
    //        signer.KeccakEthereumPersonalSign(sg, AddressWithKeyHolder.getKey(sg.getAddress()));
    //      } else {
    //        signer.Secp256Blake2bSign(sg, AddressWithKeyHolder.getKey(sg.getAddress()));
    //      }
    //    }
    //
    //    Transaction tx = signer.buildTx();
    //    return tx;
    return null;
  }
}
