package org.nervos.ckb.transaction;

import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.scriptHandler.ScriptHandler;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.Numeric;

import java.math.BigInteger;
import java.util.*;

import static org.nervos.ckb.utils.AmountUtils.dataToSudtAmount;
import static org.nervos.ckb.utils.AmountUtils.sudtAmountToData;

public class SudtTransactionBuilder extends AbstractTransactionBuilder {
  public SudtTransactionBuilder(Iterator<TransactionInput> availableInputs) {
    super(availableInputs);
  }

  @Override
  public SudtTransactionBuilder registerScriptHandler(ScriptHandler scriptHandler) {
    scriptHandlers.add(scriptHandler);
    return this;
  }

  public SudtTransactionBuilder setFeeRate(long feeRate) {
    this.feeRate = feeRate;
    return this;
  }

  public SudtTransactionBuilder addHeaderDep(byte[] headerDep) {
    tx.headerDeps.add(headerDep);
    return this;
  }

  public SudtTransactionBuilder addHeaderDep(String headerDep) {
    return addHeaderDep(Numeric.hexStringToByteArray(headerDep));
  }

  public SudtTransactionBuilder setOutputs(List<CellOutput> outputs, List<byte[]> outputsData) {
    tx.outputs.addAll(outputs);
    tx.outputsData.addAll(outputsData);
    return this;
  }

  public SudtTransactionBuilder addOutput(CellOutput output, byte[] data) {
    tx.outputs.add(output);
    tx.outputsData.add(data);
    return this;
  }

  public SudtTransactionBuilder addOutput(CellOutput output, BigInteger udtAmount) {
    byte[] data = sudtAmountToData(udtAmount);
    return addOutput(output, data);
  }

  public SudtTransactionBuilder setChangeOutput(CellOutput output) {
    if (changeOutputIndex != -1) {
      throw new IllegalStateException("Change output has been set");
    }
    changeOutputIndex = tx.outputs.size();
    byte[] data = sudtAmountToData(BigInteger.ZERO);
    return addOutput(output, data);
  }

  public TransactionWithScriptGroups build(Object context) {
    Map<Script, ScriptGroup> scriptGroupMap = new HashMap<>();
    long outputsCapacity = 0L;
    BigInteger outputSudtAmount = BigInteger.ZERO;
    for (int i = 0; i < tx.outputs.size(); i++) {
      CellOutput output = tx.outputs.get(i);
      outputsCapacity += output.capacity;
      byte[] data = tx.outputsData.get(i);
      if (data.length != 0) {
        outputSudtAmount = outputSudtAmount.add(dataToSudtAmount(data));
      }
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
          handler.buildTransaction(this, scriptGroup, context);
        }
      }
    }

    tx.witnesses = new ArrayList<>();
    tx.inputs = new ArrayList<>();
    boolean enoughCapacity = false;
    long inputsCapacity = 0L;
    BigInteger inputSudtAmount = BigInteger.ZERO;
    inputsDetail = new ArrayList<>();
    int inputIndex = -1;
    while (availableInputs.hasNext()) {
      TransactionInput input = availableInputs.next();
      System.out.println(Numeric.toHexString(input.input.previousOutput.txHash));
      System.out.println(input.input.previousOutput.index);
      inputsDetail.add(input);
      tx.inputs.add(input.input);
      tx.witnesses.add(new byte[0]);
      inputIndex += 1;
      inputSudtAmount = inputSudtAmount.add(dataToSudtAmount(input.outputData));

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
        handler.buildTransaction(this, scriptGroup, context);
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
          handler.buildTransaction(this, scriptGroup, context);
        }
      }

      inputsCapacity += input.output.capacity;
      // continue to iterate if there is enough SUDT amount
      if (inputSudtAmount.compareTo(outputSudtAmount) < 0) {
        continue;
      }

      // check if there is enough capacity for output capacity and change
      long fee = calculateTxFee(tx, feeRate);
      long changeCapacity = inputsCapacity - outputsCapacity - fee;
      CellOutput changeOutput = tx.outputs.get(changeOutputIndex);
      byte[] changeOutputData = tx.outputsData.get(changeOutputIndex);
      // get back capacity and SUDT change
      if (changeCapacity >= changeOutput.occupiedCapacity(changeOutputData)) {
        tx.outputs.get(changeOutputIndex).capacity = changeCapacity;
        tx.outputsData.set(changeOutputIndex, sudtAmountToData(inputSudtAmount.subtract(outputSudtAmount)));
        enoughCapacity = true;
        break;
      }
    }

    if (!enoughCapacity) {
      throw new IllegalStateException("No enough capacity or SUDT amount");
    }
    return TransactionWithScriptGroups.builder()
        .setTxView(tx)
        .setScriptGroups(new ArrayList<>(scriptGroupMap.values()))
        .build();
  }
}
