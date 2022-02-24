package org.nervos.ckb.signature;

import org.nervos.ckb.type.transaction.Transaction;

public interface ScriptSigner {
  boolean signTx(Transaction transaction, ScriptGroup scriptGroup, Context context);
}
