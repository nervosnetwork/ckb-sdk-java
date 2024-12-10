package org.nervos.ckb.transaction.handler;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.transaction.AbstractTransactionBuilder;

/**
 * ScriptHandler implements the building logic for various scripts.
 *
 * These handlers should be registered in the transaction builder via {@link
 * org.nervos.ckb.transaction.TransactionBuilderConfiguration}.
 */
public interface ScriptHandler {
  /**
   * Build the transaction for the script.
   *
   * This function is called by transaction builder for each script group and each context.
   *
   * Generally this function should:
   *
   * <ol>
   * <li>Check the scriptGroup to determine whether to proceed with the next steps or skip them.</li>
   * <li>Add cell deps for the script.</li>
   * <li>
   *   Prefill the witness for the script so the transaction size will not
   *   increase after signing, which may invalidate the fee calculation.
   * </li>
   * </ol>
   *
   * <p>For example:</p>
   *
   * <pre>
   * {@code
   * boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context) {
   *    // Only change the transaction when the script is used.
   *    if (scriptGroup == null || !isMatched(scriptGroup.getScript())) {
   *      return false;
   *    }
   *    // Add celldeps
   *    txBuilder.addCellDeps(cellDeps);
   *
   *    // Prefill witness placeholder
   *    int witnessIndex = scriptGroup.getInputIndices().get(0);
   *    byte[] dummyWitness = new byte[8];
   *    txBuilder.setWitness(witnessIndex, WitnessArgs.Type.LOCK, dummyWitness);
   *
   *    return true;
   * }
   * }</pre>
   *
   * @param txBuilder The transaction in building.
   * @param scriptGroup Transaction builder calls this callback for each script group found in the transaction.
   *     The script handler is responsoble for determining whether it should handle the given script group.
   * @param context This is null passed from {@link org.nervos.ckb.transaction.AbstractTransactionBuilder#build()},
   *     or provided contexts in {@link org.nervos.ckb.transaction.AbstractTransactionBuilder#build(Object...)}.
   * @return {@code true} when the script handler has modified the transaction.
   *
   */
  boolean buildTransaction(AbstractTransactionBuilder txBuilder, ScriptGroup scriptGroup, Object context);

  void init(Network network);

  default boolean postBuild(int index, AbstractTransactionBuilder txBuilder, Object context) {return false;}
}
