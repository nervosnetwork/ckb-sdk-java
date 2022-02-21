package org.nervos.ckb.signature;

import org.nervos.ckb.type.Script;

public interface ScriptSignerManager {
  void register(Script script, ScriptSigner scriptSigner);

  void signTx(TransactionTemplate transactionTemplate);
}
