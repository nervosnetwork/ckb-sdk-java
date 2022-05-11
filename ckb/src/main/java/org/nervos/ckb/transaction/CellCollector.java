package org.nervos.ckb.transaction;

import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.CellInput;
import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.address.Address;

import java.util.*;

public class CellCollector {

  private Api api;

  public CellCollector(Api api) {
    this.api = api;
  }

  public CollectResult collectInputs(
      List<String> addresses,
      Transaction tx,
      long feeRate,
      int witnessPlaceHolderLength,
      Iterator<TransactionInput> iterator) {

    int normalOutputSize = tx.outputs.size();
    // If the last cellOutput's capacity is 0, then we take it as the output to receive change.
    // Otherwise, do not calculate change.
    CellOutput lastOutput = tx.outputs.get(tx.outputs.size() - 1);
    byte[] lastOutputData = tx.outputsData.get(tx.outputs.size() - 1);
    boolean haveChangeOutput = false;
    if (Long.compareUnsigned(lastOutput.capacity, 0) == 0) {
      haveChangeOutput = true;
      normalOutputSize -= 1;
    }

    long outputCapacity = 0;
    // Capacity check for non-change output cells.
    for (int i = 0; i < normalOutputSize; i++) {
      CellOutput output = tx.outputs.get(i);
      long size = output.occupiedCapacity(tx.outputsData.get(i));
      if (Long.compareUnsigned(output.capacity, size) < 0) {
        throw new RuntimeException("Cell output byte size must not be bigger than capacity");
      }
      outputCapacity += output.capacity;
    }

    Transaction transaction =
        new Transaction(
            0,
            tx.cellDeps,
            tx.headerDeps,
            new ArrayList<>(),
            tx.outputs,
            tx.outputsData,
            new ArrayList());

    Map<Script, List<CellInput>> lockScriptInputsMap = new HashMap<>();
    Map<Script, String> scriptAddressMap = new HashMap<>();
    for (String address : addresses) {
      lockScriptInputsMap.put(Address.decode(address).getScript(), new ArrayList<>());
      scriptAddressMap.put(Address.decode(address).getScript(), address);
    }

    long inputsCapacity = 0;
    long changeCapacity = 0;
    boolean capacityEnough = false;
    while (iterator.hasNext()) {
      TransactionInput transactionInput = iterator.next();
      List<CellInput> cellInputList = lockScriptInputsMap.get(transactionInput.output.lock);
      transaction.inputs.add(transactionInput.input);
      byte[] witness = new byte[0];
      // Put witness placeholder for the first one in input group
      if (cellInputList.isEmpty()) {
        witness = new byte[witnessPlaceHolderLength];
      }
      transaction.witnesses.add(witness);
      cellInputList.add(transactionInput.input);

      inputsCapacity += transactionInput.output.capacity;
      long usedCapacity = outputCapacity + calculateTxFee(transaction, feeRate);
      // End input collection in two cases:
      // #1 inputCapacity is greater than usedCapacity when change is not needed.
      // #2 inputCapacity is greater than usedCapacity and the changeCapacity is greater than the
      // last output's (the output to receive change) occupied size when change is needed.
      if (Long.compareUnsigned(inputsCapacity, usedCapacity) >= 0) {
        if (haveChangeOutput) {
          changeCapacity = inputsCapacity - usedCapacity;
          long size = lastOutput.occupiedCapacity(lastOutputData);
          if (Long.compareUnsigned(changeCapacity, size) >= 0) {
            capacityEnough = true;
            break;
          }
        } else {
          capacityEnough = true;
          break;
        }
      }
    }

    if (!capacityEnough) {
      throw new RuntimeException("Capacity is not enough.");
    }

    List<CellsWithAddress> cellsWithAddresses = new ArrayList<>();
    for (Map.Entry<Script, List<CellInput>> entry : lockScriptInputsMap.entrySet()) {
      cellsWithAddresses.add(
          new CellsWithAddress(entry.getValue(), scriptAddressMap.get(entry.getKey())));
    }

    return new CollectResult(cellsWithAddresses, changeCapacity);
  }

  private long calculateTxFee(Transaction transaction, long feeRate) {
    return Calculator.calculateTransactionFee(transaction, feeRate);
  }
}
