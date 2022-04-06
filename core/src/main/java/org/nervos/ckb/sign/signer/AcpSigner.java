package org.nervos.ckb.sign.signer;

import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.ScriptSigner;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;

public class AcpSigner implements ScriptSigner {
  private Secp256k1Blake160SighashAllSigner secp256K1Blake160SighashAllSigner =
      Secp256k1Blake160SighashAllSigner.getInstance();
  private static AcpSigner INSTANCE;

  private AcpSigner() {}

  public static AcpSigner getInstance() {
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

  public boolean isMatched(String privateKey, byte[] scriptArgs) {
    return secp256K1Blake160SighashAllSigner.isMatched(privateKey, scriptArgs);
  }
}
