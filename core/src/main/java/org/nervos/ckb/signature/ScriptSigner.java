package org.nervos.ckb.signature;

import org.nervos.ckb.type.transaction.Transaction;

public interface ScriptSigner {
  boolean canSign(String scriptArgs, Object context);

  void signTx(Transaction transaction, ScriptGroup scriptGroup, Object context);
}
