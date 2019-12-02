package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.*;
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
  private boolean skipDataAndType = true;

  public CellCollectorWithIndexer(Api api) {
    this.api = api;
  }

  public CellCollectorWithIndexer(Api api, boolean skipDataAndType) {
    this.api = api;
    this.skipDataAndType = skipDataAndType;
  }

  public CollectResult collectInputs(
      List<String> addresses,
      List<CellOutput> cellOutputs,
      BigInteger feeRate,
      int initialLength,
      List<CellDep> cellDeps,
      List<String> outputsData)
      throws IOException {
    List<String> lockHashes = new ArrayList<>();
    for (String address : addresses) {
      AddressParseResult addressParseResult = AddressParser.parse(address);
      lockHashes.add(addressParseResult.script.computeHash());
    }
    List<String> cellOutputsData = new ArrayList<>();
    for (int i = 0; i < cellOutputs.size() - 1; i++) {
      BigInteger size = cellOutputs.get(i).occupiedCapacity("0x");
      if (size.compareTo(Numeric.toBigInt(cellOutputs.get(i).capacity)) > 0) {
        throw new IOException("Cell output byte size must not be bigger than capacity");
      }
      cellOutputsData.add("0x");
    }
    SystemScriptCell systemScriptCell = SystemContract.getSystemSecpCell(api);
    cellOutputsData.add("0x");

    if (outputsData != null && outputsData.size() > 0) {
      cellOutputsData = outputsData;
    }

    List<CellDep> cellDepList =
        Collections.singletonList(new CellDep(systemScriptCell.outPoint, CellDep.DEP_GROUP));
    if (cellDeps != null && cellDeps.size() > 0) {
      cellDepList = cellDeps;
    }

    Transaction transaction =
        new Transaction(
            "0",
            cellDepList,
            Collections.emptyList(),
            Collections.emptyList(),
            cellOutputs,
            cellOutputsData,
            Collections.emptyList());

    BigInteger inputsCapacity = BigInteger.ZERO;
    List<CellInput> cellInputs = new ArrayList<>();
    Map<String, List<CellInput>> lockInputsMap = new HashMap<>();
    for (String lockHash : lockHashes) {
      lockInputsMap.put(lockHash, new ArrayList<>());
    }
    List witnesses = new ArrayList<>();

    CellOutput changeOutput = cellOutputs.get(cellOutputs.size() - 1);

    BigInteger needCapacity = BigInteger.ZERO;
    for (CellOutput cellOutput : cellOutputs) {
      needCapacity = needCapacity.add(Numeric.toBigInt(cellOutput.capacity));
    }
    List<LiveCell> liveCells;
    for (int index = 0; index < lockHashes.size(); index++) {
      long pageNumber = 0;

      while (inputsCapacity.compareTo(needCapacity.add(calculateTxFee(transaction, feeRate))) < 0) {
        liveCells =
            api.getLiveCellsByLockHash(
                lockHashes.get(index),
                String.valueOf(pageNumber),
                String.valueOf(PAGE_SIZE),
                false);
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
          List<CellInput> cellInputList = lockInputsMap.get(lockHashes.get(index));
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
            for (String lockHash : lockHashes) {
              if (lockInputsMap.get(lockHash).size() == 0) break;
              witnesses.set(witnessIndex, new Witness(NumberUtils.getZeros(initialLength)));
              witnessIndex += lockInputsMap.get(lockHash).size();
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
