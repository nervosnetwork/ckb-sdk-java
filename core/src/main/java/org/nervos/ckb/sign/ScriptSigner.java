package org.nervos.ckb.sign;

import org.nervos.ckb.type.Transaction;

/**
 * ScriptSigner implements the signing logic for various scripts.
 *
 * These signers should be registered in {@link TransactionSigner}.
 */
public interface ScriptSigner {
  /**
   * Sign the transaction for the script.
   *
   * This function is called by {@link TransactionSigner}
   * for each context and each matched script group.
   *
   * @param transaction The transaction to be signed.
   * @param scriptGroup TransactionSigner selects the corresponding ScriptSigner for each script group.
   *     This selection is based on the registered ScriptSigner's provided script type and hash.
   * @param context This is passed from {@code TransactionSigner.signTransaction}
   * @return {@code true} when the script handler has modified the transaction.
   *
   * @see TransactionSigner#signTransaction(TransactionWithScriptGroups, Context...)
   * @see TransactionSigner#signTransaction(TransactionWithScriptGroups, Set<Context>)
   * @see TransactionSigner#signTransaction(TransactionWithScriptGroups, String...)
   */
  boolean signTransaction(Transaction transaction, ScriptGroup scriptGroup, Context context);
}
