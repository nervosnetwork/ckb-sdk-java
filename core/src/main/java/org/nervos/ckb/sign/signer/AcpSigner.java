package org.nervos.ckb.sign.signer;

import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;

public class AcpSigner implements ScriptSigner {
  private Secp256k1Blake160SighashAllSigner secp256K1Blake160SighashAllSigner =
      Secp256k1Blake160SighashAllSigner.getINSTANCE();
  private static AcpSigner INSTANCE;

  private AcpSigner() {}

  public static AcpSigner getINSTANCE() {
    if (INSTANCE == null) {
      INSTANCE = new AcpSigner();
    }
    return INSTANCE;
  }

  @Override
  public boolean signTransaction(
      Transaction transaction, ScriptGroup scriptGroup, Context context) {
    Script script = scriptGroup.getScript();
    String privateKey = context.getPrivateKey();
    if (isMatched(privateKey, script.args)) {
      return secp256K1Blake160SighashAllSigner.signScriptGroup(
          transaction, scriptGroup, privateKey);
    } else {
      return false;
    }
  }

  public boolean isMatched(String privateKey, String scriptArgs) {
    return secp256K1Blake160SighashAllSigner.isMatched(
        privateKey, Numeric.cleanHexPrefix(scriptArgs).substring(0, 40));
  }
}
