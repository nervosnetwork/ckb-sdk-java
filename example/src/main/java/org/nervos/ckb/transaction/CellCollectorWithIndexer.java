package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.cell.LiveCell;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;
import org.nervos.ckb.utils.Strings;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellCollectorWithIndexer {

  private static final int PAGE_SIZE = 50;

  private Api api;
  private boolean skipDataAndType;

  public CellCollectorWithIndexer(Api api) {
    this.api = api;
    this.skipDataAndType = true;
  }

  public CellCollectorWithIndexer(Api api, boolean skipDataAndType) {
    this.api = api;
    this.skipDataAndType = skipDataAndType;
  }

  public CollectResult collectInputs(
      List<String> addresses, Transaction tx, BigInteger feeRate, int initialLength)
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
    List<LiveCell> liveCells;
    for (String lockHash : lockHashes) {
      long pageNumber = 0;
      while (inputsCapacity.compareTo(needCapacity.add(calculateTxFee(transaction, feeRate))) < 0) {
        liveCells =
            api.getLiveCellsByLockHash(
                lockHash, String.valueOf(pageNumber), String.valueOf(PAGE_SIZE), false);
        if (liveCells == null || liveCells.size() == 0) break;
        for (LiveCell liveCell : liveCells) {
          if (skipDataAndType) {
            CellWithStatus cellWithStatus =
                api.getLiveCell(
                    new OutPoint(liveCell.createdBy.txHash, liveCell.createdBy.index), true);
            String outputsDataContent = cellWithStatus.cell.data.content;
            CellOutput cellOutput = cellWithStatus.cell.output;
            if ((!Strings.isEmpty(outputsDataContent) && !"0x".equals(outputsDataContent))
                || cellOutput.type != null) {
              continue;
            }
          }
          CellInput cellInput =
              new CellInput(
                  new OutPoint(liveCell.createdBy.txHash, liveCell.createdBy.index), "0x0");
          inputsCapacity = inputsCapacity.add(Numeric.toBigInt(liveCell.cellOutput.capacity));
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
          if (inputsCapacity.compareTo(sumNeedCapacity) > 0) {
            // update witness of group first element
            int witnessIndex = 0;
            for (String hash : lockHashes) {
              if (lockInputsMap.get(hash).size() == 0) break;
              witnesses.set(witnessIndex, new Witness(NumberUtils.getZeros(initialLength)));
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
        pageNumber += 1;
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
    return new CollectResult(cellsWithAddresses, Numeric.toHexStringWithPrefix(changeCapacity));
  }

  private BigInteger calculateTxFee(Transaction transaction, BigInteger feeRate) {
    int txSize = Serializer.serializeTransaction(transaction).toBytes().length;
    return Calculator.calculateTransactionFee(BigInteger.valueOf(txSize), feeRate);
  }

  public BigInteger getCapacityWithAddress(String address) throws IOException {
    AddressParseResult addressParseResult = AddressParser.parse(address);
    return getCapacityWithLockHash(addressParseResult.script.computeHash());
  }

  public BigInteger getCapacityWithLockHash(String lockHash) throws IOException {
    BigInteger capacity = BigInteger.ZERO;
    long pageNumber = 0;
    List<LiveCell> liveCells;
    while (true) {
      liveCells =
          api.getLiveCellsByLockHash(
              lockHash, String.valueOf(pageNumber), String.valueOf(PAGE_SIZE), false);
      if (liveCells == null || liveCells.size() == 0) break;
      for (LiveCell liveCell : liveCells) {
        if (skipDataAndType) {
          CellWithStatus cellWithStatus =
              api.getLiveCell(
                  new OutPoint(liveCell.createdBy.txHash, liveCell.createdBy.index), true);
          String outputsDataContent = cellWithStatus.cell.data.content;
          CellOutput cellOutput = cellWithStatus.cell.output;
          if ((!Strings.isEmpty(outputsDataContent) && !"0x".equals(outputsDataContent))
              || cellOutput.type != null) {
            continue;
          }
        }
        capacity = capacity.add(Numeric.toBigInt(liveCell.cellOutput.capacity));
      }
      pageNumber += 1;
    }
    return capacity;
  }

  private BigInteger calculateOutputSize(CellOutput cellOutput) {
    return BigInteger.valueOf(Serializer.serializeCellOutput(cellOutput).getLength());
  }
}
