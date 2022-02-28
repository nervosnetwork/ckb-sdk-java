package org.nervos.ckb.signature;

import org.nervos.ckb.type.transaction.Transaction;

public interface ScriptUnlocker {
  boolean unlockScript(Transaction transaction, ScriptGroup scriptGroup, Context context);
}
