package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;
import org.nervos.ckb.utils.Utils;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellCollector {

  private Api api;

  public CellCollector(Api api) {
    this.api = api;
  }

  public CollectResult collectInputs(
      List<String> addresses,
      Transaction tx,
      BigInteger feeRate,
      int initialLength,
      Iterator<TransactionInput> iterator)
      throws IOException {

    List<String> lockHashes = new ArrayList<>();
    for (String address : addresses) {
      AddressParseResult addressParseResult = AddressParser.parse(address);
      lockHashes.add(addressParseResult.script.computeHash());
    }
    Map<String, List<CellInput>> lockInputsMap = new HashMap<>();
    for (String lockHash : lockHashes) {
      lockInputsMap.put(lockHash, new ArrayList<>());
    }
    final List<CellInput> cellInputs = new ArrayList<>();

    for (int i = 0; i < tx.outputs.size() - 1; i++) {
      BigInteger size = tx.outputs.get(i).occupiedCapacity("0x");
      if (size.compareTo(Numeric.toBigInt(tx.outputs.get(i).capacity)) > 0) {
        throw new IOException("Cell output byte size must not be bigger than capacity");
      }
    }

    Transaction transaction =
        new Transaction(
            "0",
            tx.cellDeps,
            tx.headerDeps,
            tx.inputs,
            tx.outputs,
            tx.outputsData,
            Collections.emptyList());

    BigInteger inputsCapacity = BigInteger.ZERO;
    for (CellInput cellInput : tx.inputs) {
      cellInputs.add(cellInput);

      CellWithStatus cellWithStatus = api.getLiveCell(cellInput.previousOutput, false);
      inputsCapacity = inputsCapacity.add(Numeric.toBigInt(cellWithStatus.cell.output.capacity));
    }
    final List witnesses = new ArrayList<>();

    CellOutput changeOutput = tx.outputs.get(tx.outputs.size() - 1);

    BigInteger needCapacity = BigInteger.ZERO;
    for (CellOutput cellOutput : tx.outputs) {
      needCapacity = needCapacity.add(Numeric.toBigInt(cellOutput.capacity));
    }

    while (iterator.hasNext()) {
      TransactionInput transactionInput = iterator.next();
      if (transactionInput == null) break;
      CellInput cellInput = transactionInput.input;
      inputsCapacity = inputsCapacity.add(transactionInput.capacity);
      List<CellInput> cellInputList = lockInputsMap.get(transactionInput.lockHash);
      cellInputList.add(cellInput);
      cellInputs.add(cellInput);
      witnesses.add("0x");
      transaction.inputs = cellInputs;
      transaction.witnesses = witnesses;
      BigInteger sumNeedCapacity =
          needCapacity
              .add(calculateTxFee(transaction, feeRate))
              .add(calculateOutputSize(changeOutput));
      if (inputsCapacity.compareTo(sumNeedCapacity) > 0) {
        // update witness of group first element
        int witnessIndex = 0;
        for (String hash : lockHashes) {
          if (lockInputsMap.get(hash).size() == 0) break;
          witnesses.set(witnessIndex, new Witness(getZeros(initialLength)));
          witnessIndex += lockInputsMap.get(hash).size();
        }

        transaction.witnesses = witnesses;
        // calculate sum need capacity again
        sumNeedCapacity =
            needCapacity
                .add(calculateTxFee(transaction, feeRate))
                .add(calculateOutputSize(changeOutput));
        if (inputsCapacity.compareTo(sumNeedCapacity) > 0) {
          break;
        }
      }
    }

    if (inputsCapacity.compareTo(needCapacity.add(calculateTxFee(transaction, feeRate))) < 0) {
      throw new IOException("Capacity not enough!");
    }
    BigInteger changeCapacity =
        inputsCapacity.subtract(needCapacity.add(calculateTxFee(transaction, feeRate)));
    List<CellsWithAddress> cellsWithAddresses = new ArrayList<>();
    for (Map.Entry<String, List<CellInput>> entry : lockInputsMap.entrySet()) {
      cellsWithAddresses.add(
          new CellsWithAddress(
              entry.getValue(), addresses.get(lockHashes.indexOf(entry.getKey()))));
    }
    if (tx.inputs != null && tx.inputs.size() > 0) {
      cellsWithAddresses.get(0).inputs.addAll(0, tx.inputs);
    }
    return new CollectResult(cellsWithAddresses, Numeric.toHexStringWithPrefix(changeCapacity));
  }

  private BigInteger calculateTxFee(Transaction transaction, BigInteger feeRate) {
    return Calculator.calculateTransactionFee(transaction, feeRate);
  }

  private BigInteger calculateOutputSize(CellOutput cellOutput) {
    return Utils.ckbToShannon(Serializer.serializeCellOutput(cellOutput).getLength());
  }

  private String getZeros(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append("0");
    }
    return sb.toString();
  }
}
