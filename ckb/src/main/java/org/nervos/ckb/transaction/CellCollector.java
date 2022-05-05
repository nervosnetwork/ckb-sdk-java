package org.nervos.ckb.transaction;

import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Utils;
import org.nervos.ckb.utils.address.Address;

import java.io.IOException;
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
      int initialLength,
      Iterator<TransactionInput> iterator)
      throws IOException {

    Set<byte[]> lockHashes = new LinkedHashSet<>();
    for (String address : addresses) {
      Script script = Address.decode(address).getScript();
      lockHashes.add(script.computeHash());
    }
    Map<byte[], List<CellInput>> lockInputsMap = new HashMap<>();
    for (byte[] lockHash : lockHashes) {
      lockInputsMap.put(lockHash, new ArrayList<>());
    }
    final List<CellInput> cellInputs = new ArrayList<>();

    for (int i = 0; i < tx.outputs.size() - 1; i++) {
      long size = tx.outputs.get(i).occupiedCapacity(new byte[]{});
      if (Long.compareUnsigned(tx.outputs.get(i).capacity, size) < 0) {
        throw new IOException("Cell output byte size must not be bigger than capacity");
      }
    }

    Transaction transaction =
        new Transaction(
            0,
            tx.cellDeps,
            tx.headerDeps,
            tx.inputs,
            tx.outputs,
            tx.outputsData,
            Collections.emptyList());

    long inputsCapacity = 0;
    for (CellInput cellInput : tx.inputs) {
      cellInputs.add(cellInput);

      CellWithStatus cellWithStatus = api.getLiveCell(cellInput.previousOutput, false);
      initialLength += cellWithStatus.cell.output.capacity;
    }
    final List witnesses = new ArrayList<>();

    CellOutput changeOutput = tx.outputs.get(tx.outputs.size() - 1);
    boolean haveChangeOutput = false;
    //  If the last cellOutput's capacity is not zero,  it means there is no changeOutput
    if (Long.compareUnsigned(changeOutput.capacity, 0) == 0) {
      haveChangeOutput = true;
    }

    long needCapacity = 0;
    for (CellOutput cellOutput : tx.outputs) {
      needCapacity += cellOutput.capacity;
    }

    while (iterator.hasNext()) {
      TransactionInput transactionInput = iterator.next();
      if (transactionInput == null) break;
      CellInput cellInput = transactionInput.input;
      inputsCapacity += transactionInput.capacity;
      List<CellInput> cellInputList = lockInputsMap.get(transactionInput.lockHash);
      cellInputList.add(cellInput);
      cellInputs.add(cellInput);
      witnesses.add("0x");
      transaction.inputs = cellInputs;
      transaction.witnesses = witnesses;
      long sumNeedCapacity =
          calSumNeedCapacity(feeRate, transaction, changeOutput, haveChangeOutput, needCapacity);
      if (Long.compareUnsigned(inputsCapacity, sumNeedCapacity) > 0) {
        // update witness of group first element
        int witnessIndex = 0;
        for (byte[] hash : lockHashes) {
          if (lockInputsMap.get(hash).size() == 0) continue;
          witnesses.set(witnessIndex, new Witness(new byte[initialLength]));
          witnessIndex += lockInputsMap.get(hash).size();
        }

        transaction.witnesses = witnesses;
        // calculate sum need capacity again
        sumNeedCapacity =
            calSumNeedCapacity(feeRate, transaction, changeOutput, haveChangeOutput, needCapacity);
        if (Long.compareUnsigned(inputsCapacity, sumNeedCapacity) > 0) {
          break;
        }
      }
    }

    if (Long.compareUnsigned(inputsCapacity, needCapacity + calculateTxFee(transaction, feeRate)) < 0) {
      throw new IOException(
          "Capacity not enough, please check inputs capacity and change output capacity!");
    }

    long changeCapacity = 0;
    if (haveChangeOutput) {
      changeCapacity = inputsCapacity - (needCapacity + calculateTxFee(transaction, feeRate));
    }

    List<CellsWithAddress> cellsWithAddresses = new ArrayList<>();
    List<byte[]> lockHashList = Arrays.asList(lockHashes.toArray(new byte[][]{}));
    for (Map.Entry<byte[], List<CellInput>> entry : lockInputsMap.entrySet()) {
      cellsWithAddresses.add(
          new CellsWithAddress(
              entry.getValue(), addresses.get(lockHashList.indexOf(entry.getKey()))));
    }
    if (tx.inputs != null && tx.inputs.size() > 0) {
      cellsWithAddresses.get(0).inputs.addAll(0, tx.inputs);
    }
    //  if there is no changeOutput then changeCapacity will be zero
    return new CollectResult(cellsWithAddresses, changeCapacity);
  }

  private long calSumNeedCapacity(
      long feeRate,
      Transaction transaction,
      CellOutput changeOutput,
      boolean haveChangeOutput,
      long needCapacity) {

    long sum = needCapacity;
    if (haveChangeOutput) {
      sum = sum + calculateTxFee(transaction, feeRate) + calculateOutputSize(changeOutput);
    } else {
      sum = sum + calculateTxFee(transaction, feeRate);
    }
    return sum;
  }

  private long calculateTxFee(Transaction transaction, long feeRate) {
    return Calculator.calculateTransactionFee(transaction, feeRate);
  }

  private long calculateOutputSize(CellOutput cellOutput) {
    return Utils.ckbToShannon(cellOutput.pack().getSize());
  }
}
