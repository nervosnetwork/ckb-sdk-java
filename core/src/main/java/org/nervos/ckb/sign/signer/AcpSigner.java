package org.nervos.ckb.sign.signer;

import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Transaction;

public class AcpSigner implements ScriptSigner {
  @Override
  public boolean signTransaction(
      Transaction transaction, ScriptGroup scriptGroup, Context context) {
    Script script = scriptGroup.getScript();
    ECKeyPair keyPair = context.getKeyPair();
    if (isMatched(keyPair, script.args)) {
      return Secp256k1Blake160SighashAllSigner.signTransactionInPlace(
          transaction, scriptGroup, keyPair);
    } else {
      return false;
    }
  }

  public static boolean isMatched(ECKeyPair keyPair, byte[] scriptArgs) {
    return Secp256k1Blake160SighashAllSigner.isMatched(keyPair, scriptArgs);
  }
}
