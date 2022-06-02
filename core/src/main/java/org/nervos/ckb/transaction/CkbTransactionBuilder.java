package org.nervos.ckb.transaction;

import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.scriptHandler.ScriptHandler;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Utils;
import org.nervos.ckb.utils.address.Address;

import java.util.*;

public class CkbTransactionBuilder {
  private int changeOutputIndex = -1;
  private long feeRate = 1000;

  private List<ScriptHandler> scriptHandlers = new ArrayList<>();
  private Transaction tx;
  private Iterator<TransactionInput> availableInputs;

  public CkbTransactionBuilder(Iterator<TransactionInput> availableInputs) {
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

  public CkbTransactionBuilder registerScriptHandler(ScriptHandler scriptHandler) {
    scriptHandlers.add(scriptHandler);
    return this;
  }

  public CkbTransactionBuilder setFeeRate(long feeRate) {
    this.feeRate = feeRate;
    return this;
  }

  public CkbTransactionBuilder setOutputs(List<CellOutput> outputs, List<byte[]> outputsData) {
    tx.outputs.addAll(outputs);
    tx.outputsData.addAll(outputsData);
    return this;
  }

  public CkbTransactionBuilder addOutput(CellOutput output, byte[] data) {
    tx.outputs.add(output);
    tx.outputsData.add(data);
    return this;
  }

  public CkbTransactionBuilder addOutput(String address, double capacityInBytes) {
    CellOutput output = new CellOutput(Utils.ckbToShannon(capacityInBytes),
                                       Address.decode(address).getScript());
    return addOutput(output, new byte[0]);
  }

  public CkbTransactionBuilder setChangeOutpoint(CellOutput output, byte[] data) {
    if (changeOutputIndex != -1) {
      throw new IllegalStateException("Change output has been set");
    }
    changeOutputIndex = tx.outputs.size();
    return addOutput(output, data);
  }

  public CkbTransactionBuilder setChangeOutpoint(String address) {
    CellOutput output = new CellOutput(0, Address.decode(address).getScript());
    return setChangeOutpoint(output, new byte[0]);
  }

  public TransactionWithScriptGroups build() {
    Set<CellDep> cellDeps = new HashSet<>();
    long outputsCapacity = 0L;
    for (CellOutput output: tx.outputs) {
      outputsCapacity += output.capacity;
      Script script = output.type;
      for (ScriptHandler handler : scriptHandlers) {
        if (handler.isMatched(script)) {
          cellDeps.addAll(handler.getCellDeps());
        }
      }
    }

    tx.witnesses = new ArrayList<>();
    tx.inputs = new ArrayList<>();
    boolean enoughCapacity = false;
    Map<Script, ScriptGroup> scriptGroupMap = new HashMap<>();
    long inputsCapacity = 0L;
    int inputIndex = -1;
    while (availableInputs.hasNext()) {
      TransactionInput input = availableInputs.next();
      byte[] witness = new byte[0];
      inputIndex ++;

      Script lock = input.output.lock;
      ScriptGroup scriptGroup = scriptGroupMap.get(lock);
      // only set witness for first appearance of lock in inputs
      if (scriptGroup == null) {
        scriptGroup = new ScriptGroup();
        scriptGroup.setScript(lock);
        scriptGroup.setGroupType(ScriptType.LOCK);
        scriptGroup.getInputIndices().add(inputIndex);
        scriptGroupMap.put(lock, scriptGroup);
        // add cellDeps and init witness placeholder
        for (ScriptHandler handler : scriptHandlers) {
          if (handler.isMatched(lock)) {
            cellDeps.addAll(handler.getCellDeps());
            witness = handler.getWitnessPlaceholder(witness);
          }
        }
      } else {
        scriptGroup.getInputIndices().add(inputIndex);
      }

      // set transaction
      tx.inputs.add(input.input);
      tx.witnesses.add(witness);
      tx.cellDeps = new ArrayList<>(cellDeps);
      inputsCapacity += input.output.capacity;

      // check if there is enough capacity for output capacity and change
      long fee = calculateTxFee(tx, feeRate);
      long changeCapacity = inputsCapacity - outputsCapacity - fee;
      CellOutput changeOutput = tx.outputs.get(changeOutputIndex);
      byte[] changeOutputData = tx.outputsData.get(changeOutputIndex);
      if (changeCapacity >= changeOutput.occupiedCapacity(changeOutputData)) {
        tx.outputs.get(changeOutputIndex).capacity = changeCapacity;
        enoughCapacity = true;
        break;
      }
    }

    if (!enoughCapacity) {
      throw new IllegalStateException("Not enough capacity");
    }
    return TransactionWithScriptGroups.builder()
        .setTxView(tx)
        .setScriptGroups(new ArrayList<>(scriptGroupMap.values()))
        .build();
  }

  private long calculateTxFee(Transaction transaction, long feeRate) {
    return Calculator.calculateTransactionFee(transaction, feeRate);
  }
}
