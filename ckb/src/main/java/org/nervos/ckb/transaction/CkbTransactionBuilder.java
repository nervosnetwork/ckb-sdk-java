package org.nervos.ckb.transaction;

import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.handler.DaoScriptHandler;
import org.nervos.ckb.transaction.handler.ScriptHandler;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CkbTransactionBuilder extends AbstractTransactionBuilder {
  protected List<TransactionInput> transactionInputs = new ArrayList<>();
  protected long reward = 0;
  protected CellOutput changeOutput;
  protected byte[] changeOutputData;
  int transactionInputsIndex = 0;

  public CkbTransactionBuilder(TransactionBuilderConfiguration configuration, Iterator<TransactionInput> availableInputs) {
    super(configuration, availableInputs);
  }

  /**
   * Add a potential input for the transaction.
   * <p>
   * The input may not be actually used if there's already enough capacity for the outputs.
   * @param transactionInput The input.
   * @return The builder.
   */
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

  /**
   * Add outputs and data. The two parameters should have the same size.
   * @param outputs The outputs.
   * @param outputsData The data of the outputs.
   * @return The builder.
   */
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

  /**
   * Set possible change output. Its capacity must be 0.
   * <p>
   * Change output should be set only once.
   * @param output The change output.
   * @param data The data of the change output.
   * @return The builder.
   */
  public CkbTransactionBuilder setChangeOutput(@Nonnull CellOutput output, @Nonnull byte[] data) {
    if (changeOutput != null) {
      throw new IllegalStateException("Change output has been set");
    }
    if (output.capacity != 0) {
      throw new IllegalArgumentException("Change output capacity is not 0");
    }
    changeOutput = output;
    changeOutputData = data;
    return this;
  }

  /**
   * Set possible change output address.
   * <p>
   * Change output should be set only once.
   * @param address The address of the change output.
   * @return The builder.
   */
  public CkbTransactionBuilder setChangeOutput(@Nonnull String address) {
    CellOutput output = new CellOutput(0, Address.decode(address).getScript());
    return setChangeOutput(output, new byte[0]);
  }

  /**
   * Returns a clone of tx with the change output added.
   */
  private Transaction txWithChangeOutput() {
    List<CellOutput> outputs1 = Stream.concat(tx.outputs.stream(), Stream.of(changeOutput)).collect(Collectors.toList());
    List<byte[]> outputsData1 = Stream.concat(tx.outputsData.stream(), Stream.of(changeOutputData)).collect(Collectors.toList());
    return new Transaction(
        tx.version,
        tx.cellDeps,
        tx.headerDeps,
        tx.inputs,
        outputs1,
        outputsData1,
        tx.witnesses
    );
  }

  /**
   * Build the transaction. This will collect inputs so that there's enough capacity for the outputs.
   * <p>
   * If changeOutput is set, a change output will be added, unless forceSmallChangeAsFee is set and the change is small enough.
   * </p>
   * <p>
   * If changeOutput is not set, forceSmallChangeAsFee must be set and the change must be small enough.
   * </p>
   *
   * @throws IllegalStateException if settings are invalid or the transaction cannot be balanced.
   */
  @Override
  public TransactionWithScriptGroups build(Object... contexts) {
    if (getConfiguration().getForceSmallChangeAsFee() == null && changeOutput == null) {
      throw new IllegalStateException("Neither forceSmallChangeAsFee or changeOutput are set");
    }

    Map<Script, ScriptGroup> scriptGroupMap = new HashMap<>();
    long outputsCapacity = 0L;
    for (int i = 0; i < tx.outputs.size(); i++) {
      CellOutput output = tx.outputs.get(i);
      outputsCapacity += output.capacity;
      Script type = output.type;
      if (type != null) {
        ScriptGroup scriptGroup = scriptGroupMap.computeIfAbsent(type, ScriptGroup::new_type);
        scriptGroup.getOutputIndices().add(i);
        for (ScriptHandler handler : configuration.getScriptHandlers()) {
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
      ScriptGroup scriptGroup = scriptGroupMap.computeIfAbsent(lock, ScriptGroup::new_lock);
      scriptGroup.getInputIndices().add(inputIndex);
      // add cellDeps and set witness placeholder
      for (ScriptHandler handler : configuration.getScriptHandlers()) {
        for (Object context : contexts) {
          handler.buildTransaction(this, scriptGroup, context);
        }
      }

      Script type = input.output.type;
      if (type != null) {
        scriptGroup = scriptGroupMap.computeIfAbsent(type, ScriptGroup::new_type);
        scriptGroup.getInputIndices().add(inputIndex);
        for (ScriptHandler handler : configuration.getScriptHandlers()) {
          for (Object context : contexts) {
            handler.buildTransaction(this, scriptGroup, context);
          }
        }
      }

      inputsCapacity += input.output.capacity;
      final Long forceSmallChangeAsFee = getConfiguration().getForceSmallChangeAsFee();
      if (forceSmallChangeAsFee != null) {
        long fee = calculateTxFee(tx, configuration.getFeeRate());
        long changeCapacity = inputsCapacity - outputsCapacity - fee + reward;
        if (changeCapacity > 0 && changeCapacity <= forceSmallChangeAsFee) {
          enoughCapacity = true;
          break;
        }
      }

      if (changeOutput != null) {
        // Calculate fee with change output.
        Transaction txWithChange = txWithChangeOutput();
        long fee = calculateTxFee(txWithChange, configuration.getFeeRate());
        long changeCapacity = inputsCapacity - outputsCapacity - fee + reward;
        if (changeCapacity >= changeOutput.occupiedCapacity(changeOutputData)) {
          changeOutput.capacity = changeCapacity;
          // Replace tx with txWithChange.
          tx = txWithChange;
          enoughCapacity = true;
          break;
        }
      }
    }

    postBuild(scriptGroupMap);
    if (!enoughCapacity) {
      throw new IllegalStateException("No enough capacity");
    }
    return TransactionWithScriptGroups.builder()
        .setTxView(tx)
        .setScriptGroups(new ArrayList<>(rebuildScriptGroups(scriptGroupMap).values()))
        .build();
  }

  public void postBuild(Map<Script, ScriptGroup> scriptGroupMap) {
    for (Map.Entry<Script, ScriptGroup> entry : scriptGroupMap.entrySet()) {
      ScriptGroup old_group = entry.getValue();
      if (ScriptType.LOCK == old_group.getGroupType()) {
        continue;
      }
      for (int idx : old_group.getOutputIndices()) {
        for (ScriptHandler handler : configuration.getScriptHandlers()) {
          for (Object context : configuration.getScriptHandlers()) {
            if (handler.postBuild(idx, this, context)) {
              break;
            }
          }
        }
      }
    }
  }

  public TransactionInput next() {
    if (transactionInputsIndex < transactionInputs.size()) {
      return transactionInputs.get(transactionInputsIndex++);
    }
    if (availableInputs != null) {
      while (availableInputs.hasNext()) {
        TransactionInput input = availableInputs.next();
        if (!shouldFilterOut(input)) {
          return input;
        }
      }
    }
    return null;
  }

  private boolean shouldFilterOut(TransactionInput input) {
    OutPoint outPoint = input.input.previousOutput;
    // Filter duplicate found inputs same with customized input
    for (int i = 0; i < transactionInputs.size(); i++) {
      if (outPoint.equals(transactionInputs.get(i).input.previousOutput)) {
        return true;
      }
    }
    // only pool tx fee by null-type input
    return input.output.type != null;
  }
}
