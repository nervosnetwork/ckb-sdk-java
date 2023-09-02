package org.nervos.ckb.transaction;

import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.utils.Calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractTransactionBuilder {
  protected int changeOutputIndex = -1;
  protected TransactionBuilderConfiguration configuration;
  protected Iterator<TransactionInput> availableInputs;
  protected List<TransactionInput> inputsDetail = new ArrayList<>();
  protected Transaction tx = new Transaction();

  /**
   * Initialites the transaction builder.
   *
   * @param configuration This is the bundle of configuration for builder,
   *                      such as fee rate and registerred {@link org.nervos.ckb.transaction.handler.ScriptHandler}.
   */
  public AbstractTransactionBuilder(TransactionBuilderConfiguration configuration, Iterator<TransactionInput> availableInputs) {
    this.configuration = configuration;
    this.availableInputs = availableInputs;
  }

  public TransactionBuilderConfiguration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(TransactionBuilderConfiguration configuration) {
    this.configuration = configuration;
  }

  public void setInputSince(int index, long since) {
    tx.inputs.get(index).since = since;
  }

  public int setHeaderDep(byte[] headerDep) {
    for (int i = 0; i < tx.headerDeps.size(); i++) {
      if (Arrays.equals(tx.headerDeps.get(i), headerDep)) {
        return i;
      }
    }
    tx.headerDeps.add(headerDep);
    return tx.headerDeps.size() - 1;
  }

  public void addCellDeps(List<CellDep> cellDeps) {
    for (CellDep cellDep: cellDeps) {
      addCellDep(cellDep);
    }
  }

  public void addCellDep(CellDep cellDep) {
    for (int i = 0; i < tx.cellDeps.size(); i++) {
      if (tx.cellDeps.get(i).equals(cellDep)) {
        return;
      }
    }
    tx.cellDeps.add(cellDep);
  }

  public void setWitness(int i, WitnessArgs.Type type, byte[] data) {
    WitnessArgs witnessArgs = getWitnessArgs(i);
    switch (type) {
      case LOCK:
        witnessArgs.setLock(data);
        break;
      case INPUT_TYPE:
        witnessArgs.setInputType(data);
        break;
      case OUTPUT_TYPE:
        witnessArgs.setOutputType(data);
        break;
      default:
        throw new IllegalArgumentException("Unsupported witness type");
    }
    tx.witnesses.set(i, witnessArgs.pack().toByteArray());
  }

  private WitnessArgs getWitnessArgs(int i) {
    byte[] witness = tx.witnesses.get(i);
    WitnessArgs witnessArgs;
    if (witness == null || witness.length == 0) {
      witnessArgs = new WitnessArgs();
    } else {
      witnessArgs = WitnessArgs.unpack(witness);
    }
    return witnessArgs;
  }

  protected static long calculateTxFee(Transaction transaction, long feeRate) {
    return Calculator.calculateTransactionFee(transaction, feeRate);
  }

  /**
   * Builds the transaction with a default context for script handlers.
   *
   * This function will call script handlers in {@code TransactionConfiguration} with a
   * default context null. Use {@link #build(Object...)} to pass custom contexts.
   *
   * @see org.nervos.ckb.transaction.handler.ScriptHandler#buildTransaction(AbstractTransactionBuilder, ScriptGroup, Object)
   */
  public TransactionWithScriptGroups build() {
    return build((Object) null);
  }

  /**
   * Builds the transaction with custom contexts for script handlers.
   *
   * The contexts will be passed to the registered script handlers in the same order.
   *
   * @see org.nervos.ckb.transaction.handler.ScriptHandler#buildTransaction(AbstractTransactionBuilder, ScriptGroup, Object)
   */
  abstract TransactionWithScriptGroups build(Object... contexts);
}
