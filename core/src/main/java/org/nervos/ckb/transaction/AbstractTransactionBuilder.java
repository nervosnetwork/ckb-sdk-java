package org.nervos.ckb.transaction;

import org.nervos.ckb.transaction.scriptHandler.ScriptHandler;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.Calculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractTransactionBuilder {
  protected int changeOutputIndex = -1;
  protected long feeRate = 1000;

  protected List<ScriptHandler> scriptHandlers = new ArrayList<>();
  protected List<TransactionInput> inputsDetail;
  protected Transaction tx;
  protected Iterator<TransactionInput> availableInputs;

  public AbstractTransactionBuilder(Iterator<TransactionInput> availableInputs) {
    tx = new Transaction();
    tx.version = 0;
    tx.inputs = new ArrayList<>();
    tx.outputs = new ArrayList<>();
    tx.outputsData = new ArrayList<>();
    tx.cellDeps = new ArrayList<>();
    tx.headerDeps = new ArrayList<>();
    tx.witnesses = new ArrayList<>();
    this.availableInputs = availableInputs;
  }

  public AbstractTransactionBuilder registerScriptHandler(ScriptHandler scriptHandler) {
    scriptHandlers.add(scriptHandler);
    return this;
  }

  public long getFeeRate() {
    return feeRate;
  }

  public Transaction getTx() {
    return tx;
  }

  protected long calculateTxFee(Transaction transaction, long feeRate) {
    return Calculator.calculateTransactionFee(transaction, feeRate);
  }
}
