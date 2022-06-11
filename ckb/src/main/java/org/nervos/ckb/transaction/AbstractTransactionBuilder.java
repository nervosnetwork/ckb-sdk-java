package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.scriptHandler.*;
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
  protected long feeRate = 1000;
  protected Network network;

  protected List<ScriptHandler> scriptHandlers = new ArrayList<>();
  protected List<TransactionInput> inputsDetail = new ArrayList<>();
  protected Transaction tx = new Transaction();
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

  public AbstractTransactionBuilder(Iterator<TransactionInput> availableInputs, Network network) {
    this.availableInputs = availableInputs;
    this.network = network;
    if (network == Network.TESTNET) {
      scriptHandlers.addAll(TESTNET_SCRIPT_HANDLERS);
    } else {
      scriptHandlers.addAll(MAINNET_SCRIPT_HANDLERS);
    }
  }

  protected AbstractTransactionBuilder registerScriptHandler(ScriptHandler scriptHandler) {
    scriptHandlers.add(scriptHandler);
    return this;
  }

  public long getFeeRate() {
    return feeRate;
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
    for (CellDep cellDep : cellDeps) {
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

  public TransactionWithScriptGroups build() {
    return build((Object) null);
  }

  abstract TransactionWithScriptGroups build(Object... contexts);
}
