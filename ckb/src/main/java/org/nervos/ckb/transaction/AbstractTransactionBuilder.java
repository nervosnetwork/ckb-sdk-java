package org.nervos.ckb.transaction;

import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Calculator;

import java.util.*;

public abstract class AbstractTransactionBuilder {
  protected int changeOutputIndex = -1;
  protected TransactionBuilderConfiguration configuration;
  protected Iterator<TransactionInput> availableInputs;
  protected List<TransactionInput> inputsDetail = new ArrayList<>();
  protected Transaction tx = new Transaction();

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

  public TransactionWithScriptGroups build() {
    return build((Object) null);
  }

  public CellOutput getOutput(int i) {
    try {
      return this.tx.outputs.get(i);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public CellInput getInput(int i) {
    try {
      return this.tx.inputs.get(i);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public Map<Script, ScriptGroup> rebuildScriptGroups(Map<Script, ScriptGroup> scriptGroupMap) {
    Map<Script, ScriptGroup> ret = new HashMap<>();
    for (Map.Entry<Script, ScriptGroup> entry : scriptGroupMap.entrySet()) {
      Script key = entry.getKey();
      ScriptGroup old_group = entry.getValue();
      if (ScriptType.LOCK == old_group.getGroupType()) {
        ret.put(key, old_group);
        continue;
      }
      if (!old_group.getInputIndices().isEmpty()) {
        ScriptGroup new_group = ret.computeIfAbsent(key, ScriptGroup::new_type);
        new_group.getInputIndices().addAll(old_group.getInputIndices());
      }
      for (int idx : old_group.getOutputIndices()) {
        Script type = this.tx.outputs.get(idx).type;
        ScriptGroup new_group = ret.computeIfAbsent(type, ScriptGroup::new_type);
        new_group.getOutputIndices().add(idx);
      }
    }
    return ret;
  }
  abstract TransactionWithScriptGroups build(Object... contexts);
}
