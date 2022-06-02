package org.nervos.ckb;

import org.nervos.ckb.sign.ScriptGroup;
import org.nervos.ckb.sign.SystemContract;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.TransactionInput;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Utils;
import org.nervos.ckb.utils.address.Address;

import java.util.*;

public class SmartTransactionBuilder {
  private final static long defaultChangeCapacity = 0;
  private int changeOutputIndex = -1;
  private long feeRate = 1000;
  private Network network;
  private Transaction tx;
  private List<CellOutput> inputsDetail;
  private Iterator<TransactionInput> availableInputs;
  private Object witnessContext;

  /**
   * Build a transaction with the given inputs and outputs.
   *
   * @param network chain network
   * @param availableInputs only accepts inputs with all items having the same lock script
   */
  public SmartTransactionBuilder(Network network, Iterator<TransactionInput> availableInputs) {
    this(network, availableInputs, null);
  }

  /**
   * Build a transaction with the given inputs and outputs.
   *
   * @param network chain network
   * @param availableInputs only accepts inputs with all items having the same lock script
   * @param witnessContext a context to construct witnesses placeholder
   */
  public SmartTransactionBuilder(Network network, Iterator<TransactionInput> availableInputs,
                                 Object witnessContext) {
    this.network = network;
    tx = new Transaction();
    tx.version = 0;
    tx.inputs = new ArrayList<>();
    tx.outputs = new ArrayList<>();
    tx.outputsData = new ArrayList<>();
    tx.cellDeps = new ArrayList<>();
    tx.headerDeps = new ArrayList<>();
    tx.witnesses = new ArrayList<>();
    this.availableInputs = availableInputs;
    this.witnessContext = witnessContext;
  }

  public SmartTransactionBuilder setFeeRate(long feeRate) {
    this.feeRate = feeRate;
    return this;
  }

  public SmartTransactionBuilder setOutputs(List<CellOutput> outputs, List<byte[]> outputsData) {
    tx.outputs.addAll(outputs);
    tx.outputsData.addAll(outputsData);
    return this;
  }

  public SmartTransactionBuilder addOutput(CellOutput output, byte[] data) {
    tx.outputs.add(output);
    tx.outputsData.add(data);
    return this;
  }

  public SmartTransactionBuilder addOutput(CellOutput output) {
    return addOutput(output, new byte[0]);
  }

  public SmartTransactionBuilder addOutput(Address address, double capacityInBytes) {
    if (address.getNetwork() != network) {
      throw new IllegalArgumentException("Address network is not match");
    }
    CellOutput output = new CellOutput(Utils.ckbToShannon(capacityInBytes),
                                       address.getScript());
    return addOutput(output);
  }

  public SmartTransactionBuilder addOutput(String address, double capacityInBytes) {
    return addOutput(Address.decode(address), capacityInBytes);
  }

  public SmartTransactionBuilder setChangeOutpoint(CellOutput output, byte[] data) {
    if (changeOutputIndex != -1) {
      throw new IllegalStateException("Change output has been set");
    }
    changeOutputIndex = tx.outputs.size();
    return addOutput(output, data);
  }

  public SmartTransactionBuilder setChangeOutpoint(Script lock) {
    CellOutput output = new CellOutput(
        defaultChangeCapacity, lock);
    return setChangeOutpoint(output, new byte[0]);
  }

  public SmartTransactionBuilder setChangeOutpoint(String address) {
    return setChangeOutpoint(Address.decode(address).getScript());
  }

  public TransactionWithScriptGroups build() {
    boolean enoughCapacity = false;
    List<ScriptGroup> scriptGroups = null;
    inputsDetail = new ArrayList<>();
    tx.witnesses = new ArrayList<>();
    tx.inputs = new ArrayList<>();
    while (availableInputs.hasNext()) {
      TransactionInput input = availableInputs.next();
      tx.inputs.add(input.input);
      tx.witnesses.add(new byte[0]);
      inputsDetail.add(input.output);
      buildCellDeps();
      scriptGroups = buildScriptGroups();
      buildWitnesses(scriptGroups);
      enoughCapacity = doMakeChange();
      if (enoughCapacity) {
        break;
      }
    }
    if (!enoughCapacity) {
      throw new IllegalStateException("Not enough capacity");
    }
    return TransactionWithScriptGroups.builder()
        .setTxView(tx)
        .setScriptGroups(scriptGroups)
        .build();
  }

  private List<ScriptGroup> buildScriptGroups() {
    List<ScriptGroup> scriptGroups = new ArrayList<>();
    Map<Script, List<Integer>> lockScriptGroup = new HashMap<>();
    for (int i = 0; i < inputsDetail.size(); i++) {
      CellOutput output = inputsDetail.get(i);
      Script script = output.lock;
      if (!lockScriptGroup.containsKey(script)) {
        lockScriptGroup.put(script, new ArrayList<>());
      }
      lockScriptGroup.get(script).add(i);
    }
    for (Map.Entry<Script, List<Integer>> entry : lockScriptGroup.entrySet()) {
      ScriptGroup scriptGroup = ScriptGroup.builder()
          .setScript(entry.getKey())
          .setGroupType(ScriptType.LOCK)
          .setInputIndices(entry.getValue())
          .setOutputIndices(new ArrayList<>())
          .build();
      scriptGroups.add(scriptGroup);
    }
    return scriptGroups;
  }

  private void buildWitnesses(List<ScriptGroup> scriptGroups) {
    if (scriptGroups.size() != 1) {
      throw new IllegalStateException("Only one script group is allowed");
    }
    ScriptGroup group = scriptGroups.get(0);
    SystemContract.Type contractType = network.getSystemContractType(group.getScript());
    int i = group.getInputIndices().get(0);
    byte[] witness = tx.witnesses.get(i);
    witness = contractType.getWitnessPlaceHolder(witness, witnessContext);
    tx.witnesses.set(i, witness);
  }

  private void buildCellDeps() {
    Set<CellDep> cellDeps = new HashSet<>();
    for (CellOutput input : inputsDetail) {
      updateCellDepsByScript(cellDeps, input.lock);
      updateCellDepsByScript(cellDeps, input.type);
    }
    for (CellOutput output : tx.outputs) {
      updateCellDepsByScript(cellDeps, output.type);
    }
    tx.cellDeps = new ArrayList<>(cellDeps);
  }

  private void updateCellDepsByScript(Set<CellDep> celldeps, Script script) {
    if (script == null) {
      return;
    }
    SystemContract contract = network.getSystemContract(script);
    if (contract != null) {
      celldeps.addAll(contract.getCellDeps());
    } else {
      throw new IllegalArgumentException("Only support system contract");
    }
  }

  private boolean doMakeChange() {
    if (changeOutputIndex == -1) {
      throw new IllegalStateException("Change output has not been set");
    }
    long inputsCapacity = 0;
    for (int i = 0; i < inputsDetail.size(); i++) {
      inputsCapacity += inputsDetail.get(i).capacity;
    }
    long outputCapacity = 0;
    for (int i = 0; i < tx.outputs.size(); i++) {
      outputCapacity += tx.outputs.get(i).capacity;
    }
    long fee = calculateTxFee(tx, feeRate);
    long changeCapacity = inputsCapacity - outputCapacity - fee;
    CellOutput changeOutput = tx.outputs.get(changeOutputIndex);
    byte[] changeOutputData = tx.outputsData.get(changeOutputIndex);
    if (changeCapacity >= changeOutput.occupiedCapacity(changeOutputData)) {
      tx.outputs.get(changeOutputIndex).capacity = changeCapacity;
      return true;
    } else {
      return false;
    }
  }

  private long calculateTxFee(Transaction transaction, long feeRate) {
    return Calculator.calculateTransactionFee(transaction, feeRate);
  }
}
