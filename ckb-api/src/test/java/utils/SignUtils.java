package utils;

import constant.AddressWithKeyHolder;
import java.util.List;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.mercury.model.resp.MercuryScriptGroup;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import org.nervos.mercury.model.resp.signature.HashAlgorithmEnum;
import org.nervos.mercury.model.resp.signature.SignAlgorithmEnum;
import org.nervos.mercury.signature.TransactionSigner;

/** @author zjh @Created Date: 2021/7/23 @Description: @Modify by: */
public class SignUtils {
  public static Transaction sign(TransactionCompletionResponse s) {
    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    TransactionSigner signer = new TransactionSigner(s.txView);

    for (MercuryScriptGroup sg : scriptGroups) {
      if (isPWEthereum(sg)) {
        signer.KeccakEthereumPersonalSign(sg, AddressWithKeyHolder.getKey(sg.getAddress()));
      } else {
        signer.Secp256Blake2bSign(sg, AddressWithKeyHolder.getKey(sg.getAddress()));
      }
    }

    Transaction tx = signer.buildTx();
    return tx;
  }

  public static Transaction signByKey(TransactionCompletionResponse s, String key) {
    List<MercuryScriptGroup> scriptGroups = s.getScriptGroup();
    TransactionSigner signer = new TransactionSigner(s.txView);

    for (MercuryScriptGroup sg : scriptGroups) {
      if (isPWEthereum(sg)) {
        signer.KeccakEthereumPersonalSign(sg, AddressWithKeyHolder.getKey(sg.getAddress()));
      } else {
        signer.Secp256Blake2bSign(sg, AddressWithKeyHolder.getKey(sg.getAddress()));
      }
    }

    Transaction tx = signer.buildTx();
    return tx;
  }

  private static boolean isPWEthereum(MercuryScriptGroup sg) {
    return sg.action.hashAlgorithmEnum == HashAlgorithmEnum.Keccak256
        && sg.action.signatureInfo.algorithm == SignAlgorithmEnum.EthereumPersonal;
  }
}
