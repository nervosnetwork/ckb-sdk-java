package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.handler.DaoScriptHandler;
import org.nervos.ckb.transaction.handler.ScriptHandler;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import java.util.*;

public class CkbTransactionBuilder extends AbstractTransactionBuilder {
  protected List<TransactionInput> transactionInputs = new ArrayList<>();
  protected long reward = 0;

  public CkbTransactionBuilder(Iterator<TransactionInput> availableInputs, Network network) {
    super(availableInputs, network);
  }

  public CkbTransactionBuilder(Iterator<TransactionInput> availableInputs) {
    super(availableInputs);
  }

  @Override
  public CkbTransactionBuilder registerScriptHandler(ScriptHandler scriptHandler) {
    super.registerScriptHandler(scriptHandler);
    return this;
  }

  public CkbTransactionBuilder setFeeRate(long feeRate) {
    this.feeRate = feeRate;
    return this;
  }

  public CkbTransactionBuilder addInput(TransactionInput transactionInput) {
    transactionInputs.add(transactionInput);
    return this;
  }

  public CkbTransactionBuilder addHeaderDep(byte[] headerDep) {
    tx.headerDeps.add(headerDep);
    return this;
  }

  public CkbTransactionBuilder addHeaderDep(String headerDep) {
    return addHeaderDep(Numeric.hexStringToByteArray(headerDep));
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

  public CkbTransactionBuilder addOutput(String address, long capacity) {
    CellOutput output = new CellOutput(capacity, Address.decode(address).getScript());
    return addOutput(output, new byte[0]);
  }

  public CkbTransactionBuilder addDaoDepositOutput(String address, long capacity) {

    CellOutput output = new CellOutput(capacity, Address.decode(address).getScript(),
                                       DaoScriptHandler.DAO_SCRIPT);
    byte[] data = DaoScriptHandler.DEPOSIT_CELL_DATA;
    return addOutput(output, data);
  }

  public CkbTransactionBuilder setChangeOutput(CellOutput output, byte[] data) {
    if (changeOutputIndex != -1) {
      throw new IllegalStateException("Change output has been set");
    }
    changeOutputIndex = tx.outputs.size();
    return addOutput(output, data);
  }

  public CkbTransactionBuilder setChangeOutput(String address) {
    CellOutput output = new CellOutput(0, Address.decode(address).getScript());
    return setChangeOutput(output, new byte[0]);
  }

  @Override
  public TransactionWithScriptGroups build(Object... contexts) {
    Map<Script, ScriptGroup> scriptGroupMap = new HashMap<>();
    long outputsCapacity = 0L;
    for (int i = 0; i < tx.outputs.size(); i++) {
      CellOutput output = tx.outputs.get(i);
      outputsCapacity += output.capacity;
      Script type = output.type;
      if (type != null) {
        ScriptGroup scriptGroup = scriptGroupMap.get(type);
        if (scriptGroup == null) {
          scriptGroup = new ScriptGroup();
          scriptGroup.setScript(type);
          scriptGroup.setGroupType(ScriptType.TYPE);
          scriptGroupMap.put(type, scriptGroup);
        }
        scriptGroup.getOutputIndices().add(i);
        for (ScriptHandler handler : scriptHandlers) {
          for (Object context : contexts) {
            handler.buildTransaction(this, scriptGroup, context);
          }
        }
      }
    }

    boolean enoughCapacity = false;
    long inputsCapacity = 0L;
    inputsDetail = new ArrayList<>();
    int inputIndex = -1;
    TransactionInput input;
    for (input = next(); input != null; input = next()) {
      inputsDetail.add(input);
      tx.inputs.add(input.input);
      tx.witnesses.add(new byte[0]);
      inputIndex += 1;

      Script lock = input.output.lock;
      ScriptGroup scriptGroup = scriptGroupMap.get(lock);
      if (scriptGroup == null) {
        scriptGroup = new ScriptGroup();
        scriptGroup.setScript(lock);
        scriptGroup.setGroupType(ScriptType.LOCK);
        scriptGroupMap.put(lock, scriptGroup);
      }
      scriptGroup.getInputIndices().add(inputIndex);
      // add cellDeps and set witness placeholder
      for (ScriptHandler handler : scriptHandlers) {
        for (Object context : contexts) {
          handler.buildTransaction(this, scriptGroup, context);
        }
      }

      Script type = input.output.type;
      if (type != null) {
        scriptGroup = scriptGroupMap.get(type);
        if (scriptGroup == null) {
          scriptGroup = new ScriptGroup();
          scriptGroup.setScript(type);
          scriptGroup.setGroupType(ScriptType.TYPE);
          scriptGroupMap.put(type, scriptGroup);
        }
        scriptGroup.getInputIndices().add(inputIndex);
        for (ScriptHandler handler : scriptHandlers) {
          for (Object context : contexts) {
            handler.buildTransaction(this, scriptGroup, context);
          }
        }
      }

      inputsCapacity += input.output.capacity;
      // check if there is enough capacity for output capacity and change
      long fee = calculateTxFee(tx, feeRate);
      long changeCapacity = inputsCapacity - outputsCapacity - fee + reward;
      CellOutput changeOutput = tx.outputs.get(changeOutputIndex);
      byte[] changeOutputData = tx.outputsData.get(changeOutputIndex);
      if (changeCapacity >= changeOutput.occupiedCapacity(changeOutputData)) {
        tx.outputs.get(changeOutputIndex).capacity = changeCapacity;
        enoughCapacity = true;
        break;
      }
    }

    if (!enoughCapacity) {
      throw new IllegalStateException("No enough capacity");
    }
    return TransactionWithScriptGroups.builder()
        .setTxView(tx)
        .setScriptGroups(new ArrayList<>(scriptGroupMap.values()))
        .build();
  }

  int transactionInputsIndex = 0;

  public TransactionInput next() {
    if (transactionInputsIndex < transactionInputs.size()) {
      return transactionInputs.get(transactionInputsIndex++);
    }
    if (availableInputs != null) {
      while (availableInputs.hasNext()) {
        TransactionInput input = availableInputs.next();
        if (toFilter(input)) {
          continue;
        } else {
          return input;
        }
      }
    }
    return null;
  }

  private boolean toFilter(TransactionInput input) {
    OutPoint outPoint = input.input.previousOutput;
    // Filter duplicate found inputs same with customized input
    for (int i = 0; i < transactionInputs.size(); i++) {
      if (outPoint.equals(transactionInputs.get(i).input.previousOutput)) {
        return true;
      }
    }
    // only pool tx fee by null-type input
    if (input.output.type != null) {
      return true;
    }
    return false;
  }
}
