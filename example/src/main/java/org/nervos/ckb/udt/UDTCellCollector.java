package org.nervos.ckb.udt;

import io.reactivex.annotations.NonNull;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.transaction.CellsWithAddress;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.*;
import org.nervos.ckb.type.fixed.UInt128;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class UDTCellCollector {
  private static final BigInteger MIN_UDT_CHANGE_CAPACITY = BigInteger.valueOf(142);

  private Api api;

  public UDTCellCollector(Api api) {
    this.api = api;
  }

  public UDTCollectResult collectInputs(
      List<String> addresses,
      Transaction tx,
      BigInteger feeRate,
      int initialLength,
      @NonNull String typeHash,
      BigInteger udtAmount)
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
    List<CellInput> cellInputs = new ArrayList<>();

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
    List witnesses = new ArrayList<>();

    CellOutput changeOutput = tx.outputs.get(tx.outputs.size() - 1);

    BigInteger needCapacity = BigInteger.ZERO;
    for (CellOutput cellOutput : tx.outputs) {
      needCapacity = needCapacity.add(Numeric.toBigInt(cellOutput.capacity));
    }
    List<CellOutputWithOutPoint> cellOutputList;
    BigInteger inputUdtAmount = BigInteger.ZERO;
    for (String lockHash : lockHashes) {
      long toBlockNumber = api.getTipBlockNumber().longValue();
      long fromBlockNumber = 1;

      while (fromBlockNumber <= toBlockNumber) {
        long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
        cellOutputList =
            api.getCellsByLockHash(
                lockHash,
                BigInteger.valueOf(fromBlockNumber).toString(),
                BigInteger.valueOf(currentToBlockNumber).toString());
        for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputList) {
          CellWithStatus cellWithStatus = api.getLiveCell(cellOutputWithOutPoint.outPoint, true);
          CellOutput cellOutput = cellWithStatus.cell.output;
          if (cellOutput.type == null || !typeHash.equals(cellOutput.type.computeHash())) {
            continue;
          }
          inputUdtAmount = inputUdtAmount.add(Numeric.toBigInt(cellWithStatus.cell.data.content));
          CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0x0");
          inputsCapacity = inputsCapacity.add(Numeric.toBigInt(cellOutputWithOutPoint.capacity));
          List<CellInput> cellInputList = lockInputsMap.get(lockHash);
          cellInputList.add(cellInput);
          cellInputs.add(cellInput);
          witnesses.add("0x");
          transaction.inputs = cellInputs;
          transaction.witnesses = witnesses;
          BigInteger sumNeedCapacity =
              needCapacity
                  .add(calculateTxFee(transaction, feeRate))
                  .add(calculateOutputSize(changeOutput));
          if (inputsCapacity.subtract(sumNeedCapacity).compareTo(MIN_UDT_CHANGE_CAPACITY) >= 0
              && inputUdtAmount.compareTo(udtAmount) >= 0) {
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
            if (inputsCapacity.subtract(sumNeedCapacity).compareTo(MIN_UDT_CHANGE_CAPACITY) >= 0) {
              break;
            }
          }
        }
        fromBlockNumber = currentToBlockNumber + 1;
      }
    }
    if (inputsCapacity.compareTo(needCapacity.add(calculateTxFee(transaction, feeRate))) < 0) {
      throw new IOException("Capacity not enough!");
    }
    BigInteger changeCapacity =
        inputsCapacity.subtract(needCapacity.add(calculateTxFee(transaction, feeRate)));
    BigInteger changeUdtAmount = inputUdtAmount.subtract(inputUdtAmount);
    List<CellsWithAddress> cellsWithAddresses = new ArrayList<>();
    for (Map.Entry<String, List<CellInput>> entry : lockInputsMap.entrySet()) {
      cellsWithAddresses.add(
          new CellsWithAddress(
              entry.getValue(), addresses.get(lockHashes.indexOf(entry.getKey()))));
    }
    if (tx.inputs != null && tx.inputs.size() > 0) {
      cellsWithAddresses.get(0).inputs.addAll(0, tx.inputs);
    }
    return new UDTCollectResult(
        cellsWithAddresses, Numeric.toHexStringWithPrefix(changeCapacity), changeUdtAmount);
  }

  public BigInteger getUdtBalanceWithAddress(String address, @NonNull String typeHash)
      throws IOException {
    return getUdtBalanceWithLockHash(AddressParser.parse(address).script.computeHash(), typeHash);
  }

  public BigInteger getUdtBalanceWithLockHash(String lockHash, @NonNull String typeHash)
      throws IOException {
    BigInteger udtAmount = BigInteger.ZERO;
    long toBlockNumber = api.getTipBlockNumber().longValue();
    long fromBlockNumber = 1;

    while (fromBlockNumber <= toBlockNumber) {
      long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
      List<CellOutputWithOutPoint> cellOutputs =
          api.getCellsByLockHash(
              lockHash,
              BigInteger.valueOf(fromBlockNumber).toString(),
              BigInteger.valueOf(currentToBlockNumber).toString());

      if (cellOutputs != null && cellOutputs.size() > 0) {
        for (CellOutputWithOutPoint output : cellOutputs) {
          CellWithStatus cellWithStatus = api.getLiveCell(output.outPoint, true);
          String outputsData = cellWithStatus.cell.data.content;
          CellOutput cellOutput = cellWithStatus.cell.output;
          if (cellOutput.type == null || !typeHash.equals(cellOutput.type.computeHash())) {
            continue;
          }
          udtAmount = udtAmount.add(new UInt128(outputsData).getValue());
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    return udtAmount;
  }

  private BigInteger calculateTxFee(Transaction transaction, BigInteger feeRate) {
    return Calculator.calculateTransactionFee(transaction, feeRate);
  }

  private BigInteger calculateOutputSize(CellOutput cellOutput) {
    return BigInteger.valueOf(Serializer.serializeCellOutput(cellOutput).getLength());
  }

  private String getZeros(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append("0");
    }
    return sb.toString();
  }
}
