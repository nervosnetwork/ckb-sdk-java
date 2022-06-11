package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.scriptHandler.ScriptHandler;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.address.Address;

import java.math.BigInteger;
import java.util.*;

import static org.nervos.ckb.utils.AmountUtils.dataToSudtAmount;
import static org.nervos.ckb.utils.AmountUtils.sudtAmountToData;

public class SudtTransactionBuilder extends AbstractTransactionBuilder {
  private TransactionType transactionType;
  private byte[] sudtArgs;
  private Script sudtType;

  public enum TransactionType {
    ISSUE,
    TRANSFER
  }

  public SudtTransactionBuilder(Iterator<TransactionInput> availableInputs, Network network) {
    super(availableInputs, network);
  }

  public SudtTransactionBuilder setSudtArgs(byte[] sudtArgs) {
    this.sudtArgs = sudtArgs;
    byte[] codeHash;
    if (network == Network.TESTNET) {
      codeHash = Script.SUDT_CODE_HASH_TESTNET;
    } else if (network == Network.MAINNET) {
      codeHash = Script.SUDT_CODE_HASH_MAINNET;
    } else {
      throw new IllegalArgumentException("Unsupported network");
    }
    sudtType = new Script(
        codeHash,
        sudtArgs,
        Script.HashType.TYPE);
    return this;
  }

  public SudtTransactionBuilder setSudtArgs(String sudtOwnerAddress) {
    sudtArgs = Address.decode(sudtOwnerAddress).getScript().computeHash();
    return setSudtArgs(sudtArgs);
  }

  public SudtTransactionBuilder setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
    return this;
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

  public SudtTransactionBuilder addOutput(CellOutput output, byte[] data) {
    tx.outputs.add(output);
    tx.outputsData.add(data);
    return this;
  }

  public SudtTransactionBuilder addSudtOutput(String address, long capacity, long udtAmount) {
    return addSudtOutput(address, capacity, BigInteger.valueOf(udtAmount));
  }

  public SudtTransactionBuilder addSudtOutput(String address, long capacity, BigInteger udtAmount) {
    CellOutput output = new CellOutput(
        capacity,
        Address.decode(address).getScript(),
        sudtType);
    byte[] data = sudtAmountToData(udtAmount);
    return addOutput(output, data);
  }

  public SudtTransactionBuilder setChangeOutput(String address) {
    if (changeOutputIndex != -1) {
      throw new IllegalStateException("Change output has been set");
    }
    changeOutputIndex = tx.outputs.size();
    byte[] data = sudtAmountToData(BigInteger.ZERO);
    CellOutput output = new CellOutput(
        0,
        Address.decode(address).getScript(),
        sudtType);
    return addOutput(output, data);
  }

  public TransactionWithScriptGroups build() {
    return build(null);
  }

  public TransactionWithScriptGroups build(Object context) {
    if (sudtArgs == null) {
      throw new IllegalStateException("SudtArgs is not set");
    }
    if (transactionType == null) {
      throw new IllegalStateException("TransactionType is not set");
    }
    // won't change back SUDT for issue type transaction
    if (transactionType == TransactionType.ISSUE) {
      tx.outputs.get(changeOutputIndex).type = null;
      tx.outputsData.set(changeOutputIndex, new byte[0]);
    }

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
      inputsDetail.add(input);
      tx.inputs.add(input.input);
      tx.witnesses.add(new byte[0]);
      inputIndex += 1;
      inputSudtAmount = inputSudtAmount.add(dataToSudtAmount(input.outputData));

      Script lock = input.output.lock;
      if (transactionType == TransactionType.ISSUE) {
        if (!Arrays.equals(lock.computeHash(), sudtArgs)) {
          throw new IllegalStateException("input lock hash should be the same as SUDT args in the SUDT-issue transaction");
        }
      }
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
      if (transactionType == TransactionType.TRANSFER) {
        if (inputSudtAmount.compareTo(outputSudtAmount) < 0) {
          continue;
        }
      }

      // check if there is enough capacity for output capacity and change
      long fee = calculateTxFee(tx, feeRate);
      long changeCapacity = inputsCapacity - outputsCapacity - fee;
      CellOutput changeOutput = tx.outputs.get(changeOutputIndex);
      byte[] changeOutputData = tx.outputsData.get(changeOutputIndex);
      // get back capacity and SUDT change
      if (changeCapacity >= changeOutput.occupiedCapacity(changeOutputData)) {
        tx.outputs.get(changeOutputIndex).capacity = changeCapacity;
        if (transactionType == TransactionType.TRANSFER) {
          tx.outputsData.set(changeOutputIndex, sudtAmountToData(inputSudtAmount.subtract(outputSudtAmount)));
        }
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