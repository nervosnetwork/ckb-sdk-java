package org.nervos.ckb.sign;

import org.nervos.ckb.type.Transaction;

public interface ScriptSigner {
  boolean signTransaction(Transaction transaction, ScriptGroup scriptGroup, Context context);
}
