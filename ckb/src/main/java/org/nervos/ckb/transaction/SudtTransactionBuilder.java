package org.nervos.ckb.transaction;

import org.nervos.ckb.Network;
import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.handler.ScriptHandler;
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
  private Script sudtTypeScript;

  public enum TransactionType {
    ISSUE,
    TRANSFER
  }

  public SudtTransactionBuilder(TransactionBuilderConfiguration configuration, Iterator<TransactionInput> availableInputs,
                                TransactionType transactionType, byte[] sudtArgs) {
    super(configuration, availableInputs);
    this.transactionType = transactionType;
    setSudtTypeScript(sudtArgs);
  }

  public SudtTransactionBuilder(TransactionBuilderConfiguration configuration, Iterator<TransactionInput> availableInputs,
                                TransactionType transactionType, String sudtOwnerAddress) {
    super(configuration, availableInputs);
    this.transactionType = transactionType;
    setSudtTypeScript(sudtOwnerAddress);
  }

  public void setSudtTypeScript(Script sudtTypeScript) {
    this.sudtTypeScript = sudtTypeScript;
  }

  public SudtTransactionBuilder setSudtTypeScript(byte[] sudtArgs) {
    byte[] codeHash;
    Network network = configuration.getNetwork();
    if (network == Network.TESTNET) {
      codeHash = Script.SUDT_CODE_HASH_TESTNET;
    } else if (network == Network.MAINNET) {
      codeHash = Script.SUDT_CODE_HASH_MAINNET;
    } else {
      throw new IllegalArgumentException("Unsupported network");
    }
    sudtTypeScript = new Script(
        codeHash,
        sudtArgs,
        Script.HashType.TYPE);
    return this;
  }

  public SudtTransactionBuilder setSudtTypeScript(String sudtOwnerAddress) {
    Address address = Address.decode(sudtOwnerAddress);
    byte[] sudtArgs = address.getScript().computeHash();
    return setSudtTypeScript(sudtArgs);
  }

  public SudtTransactionBuilder addOutput(CellOutput output, byte[] data) {
    tx.outputs.add(output);
    tx.outputsData.add(data);
    return this;
  }

  public SudtTransactionBuilder addSudtOutput(String address, long udtAmount) {
    return addSudtOutput(address, BigInteger.valueOf(udtAmount));
  }

  public SudtTransactionBuilder addSudtOutput(String address, BigInteger udtAmount) {
    CellOutput output = new CellOutput(
        0,
        Address.decode(address).getScript(),
        sudtTypeScript);
    byte[] data = sudtAmountToData(udtAmount);
    output.capacity = output.occupiedCapacity(data);
    return addOutput(output, data);
  }

  public SudtTransactionBuilder addSudtOutput(String address, long udtAmount, long capacity) {
    return addSudtOutput(address, BigInteger.valueOf(udtAmount), capacity);
  }

  public SudtTransactionBuilder addSudtOutput(String address, BigInteger udtAmount, long capacity) {
    CellOutput output = new CellOutput(
        capacity,
        Address.decode(address).getScript(),
        sudtTypeScript);
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
        sudtTypeScript);
    return addOutput(output, data);
  }

  @Override
  public TransactionWithScriptGroups build(Object... contexts) {
    if (sudtTypeScript == null) {
      throw new IllegalStateException("Sudt type script is not initialized");
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
        for (ScriptHandler handler: configuration.getScriptHandlers()) {
          for (Object context: contexts) {
            handler.buildTransaction(this, scriptGroup, context);
          }
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
        if (!Arrays.equals(lock.computeHash(), sudtTypeScript.args)) {
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
      for (ScriptHandler handler: configuration.getScriptHandlers()) {
        for (Object context: contexts) {
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
        for (ScriptHandler handler: configuration.getScriptHandlers()) {
          for (Object context: contexts) {
            handler.buildTransaction(this, scriptGroup, context);
          }
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
      long fee = calculateTxFee(tx, configuration.getFeeRate());
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
