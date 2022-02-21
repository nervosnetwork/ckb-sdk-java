package org.nervos.ckb.signature;

import org.nervos.ckb.type.transaction.Transaction;

public interface ScriptSigner {
  void signTx(Transaction transaction, ScriptGroup scriptGroup);
}
