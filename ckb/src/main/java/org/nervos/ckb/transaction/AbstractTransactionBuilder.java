package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.transaction.scriptHandler.*;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Calculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractTransactionBuilder {
  protected int changeOutputIndex = -1;
  protected long feeRate = 1000;
  protected Network network;

  protected List<ScriptHandler> scriptHandlers = new ArrayList<>();
  protected List<TransactionInput> inputsDetail;
  protected Transaction tx;
  protected Iterator<TransactionInput> availableInputs;

  private static List<ScriptHandler> TESTNET_SCRIPT_HANDLERS = new ArrayList<>();
  private static List<ScriptHandler> MAINNET_SCRIPT_HANDLERS = new ArrayList<>();

  static {
    TESTNET_SCRIPT_HANDLERS.add(new Secp256k1Blake160SighashAllScriptHandler(Network.TESTNET));
    TESTNET_SCRIPT_HANDLERS.add(new Secp256k1Blake160MultisigAllScriptHandler(Network.TESTNET));
    TESTNET_SCRIPT_HANDLERS.add(new SudtScriptHandler(Network.TESTNET));
    TESTNET_SCRIPT_HANDLERS.add(new DaoScriptHandler(Network.TESTNET));

    MAINNET_SCRIPT_HANDLERS.add(new Secp256k1Blake160SighashAllScriptHandler(Network.MAINNET));
    MAINNET_SCRIPT_HANDLERS.add(new Secp256k1Blake160MultisigAllScriptHandler(Network.MAINNET));
    MAINNET_SCRIPT_HANDLERS.add(new SudtScriptHandler(Network.MAINNET));
    MAINNET_SCRIPT_HANDLERS.add(new DaoScriptHandler(Network.MAINNET));
  }

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

  public AbstractTransactionBuilder(Iterator<TransactionInput> availableInputs, Network network) {
    this(availableInputs);
    this.network = network;
    if (network == Network.TESTNET) {
      scriptHandlers.addAll(TESTNET_SCRIPT_HANDLERS);
    } else {
      scriptHandlers.addAll(MAINNET_SCRIPT_HANDLERS);
    }
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
