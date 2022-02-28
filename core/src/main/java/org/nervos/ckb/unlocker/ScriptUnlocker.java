package org.nervos.ckb.unlocker;

import org.nervos.ckb.type.transaction.Transaction;

public interface ScriptUnlocker {
  boolean unlockScript(Transaction transaction, ScriptGroup scriptGroup, Context context);
}
