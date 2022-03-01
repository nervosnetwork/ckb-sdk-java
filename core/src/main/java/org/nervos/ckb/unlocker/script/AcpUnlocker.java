package org.nervos.ckb.unlocker.script;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.unlocker.Context;
import org.nervos.ckb.unlocker.ScriptGroup;
import org.nervos.ckb.unlocker.ScriptUnlocker;
import org.nervos.ckb.utils.Numeric;

public class AcpUnlocker implements ScriptUnlocker {
  private Secp256K1Blake160Unlocker secp256K1Blake160Unlocker =
      Secp256K1Blake160Unlocker.getInstance();
  private static AcpUnlocker instance;

  private AcpUnlocker() {}

  public static AcpUnlocker getInstance() {
    if (instance == null) {
      instance = new AcpUnlocker();
    }
    return instance;
  }

  @Override
  public boolean unlockScript(Transaction transaction, ScriptGroup scriptGroup, Context context) {
    Script script = scriptGroup.getScript();
    String privateKey = context.getPrivateKey();
    if (isMatched(privateKey, script.args) == false) {
      return false;
    }
    return secp256K1Blake160Unlocker.unlockScript(transaction, scriptGroup, privateKey);
  }

  public boolean isMatched(String privateKey, String scriptArgs) {
    return secp256K1Blake160Unlocker.isMatched(
        privateKey, Numeric.cleanHexPrefix(scriptArgs).substring(0, 40));
  }
}
