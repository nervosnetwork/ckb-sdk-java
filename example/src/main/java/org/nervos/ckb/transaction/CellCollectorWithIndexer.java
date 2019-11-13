package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.cell.LiveCell;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellCollectorWithIndexer {

  private static final int PAGE_SIZE = 50;

  private Api api;

  public CellCollectorWithIndexer(Api api) {
    this.api = api;
  }

  public Map<String, List<CellInput>> collectInputs(
      List<String> lockHashes, List<CellOutput> cellOutputs, BigInteger feeRate, int initialLength)
      throws IOException {
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

    Transaction transaction =
        new Transaction(
            "0",
            Collections.singletonList(new CellDep(systemScriptCell.outPoint, CellDep.DEP_GROUP)),
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
      long tipBlockNumber = api.getTipBlockNumber().longValue();
      long pageNumber = 1;

      while (pageNumber <= tipBlockNumber
          && inputsCapacity.compareTo(needCapacity.add(calculateTxFee(transaction, feeRate))) < 0) {
        liveCells =
            api.getLiveCellsByLockHash(
                lockHashes.get(index),
                String.valueOf(pageNumber),
                String.valueOf(PAGE_SIZE),
                false);
        for (LiveCell liveCell : liveCells) {
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
              witnesses.set(witnessIndex, new Witness(getZeros(initialLength)));
              witnessIndex += lockInputsMap.get(lockHash).size();
            }
            transaction.witnesses = witnesses;
            // calculate sum need capacity again
            sumNeedCapacity =
                needCapacity
                    .add(calculateTxFee(transaction, feeRate))
                    .add(calculateOutputSize(changeOutput));
            if (inputsCapacity.compareTo(sumNeedCapacity) > 0) {
              // calculate change capacity again
              changeOutput.capacity =
                  Numeric.prependHexPrefix(
                      inputsCapacity
                          .subtract(needCapacity)
                          .subtract(calculateTxFee(transaction, feeRate))
                          .toString(16));
              cellOutputs.set(cellOutputs.size() - 1, changeOutput);
              transaction.outputs = cellOutputs;
              break;
            }
          }
        }
        pageNumber += PAGE_SIZE;
      }
    }
    if (inputsCapacity.compareTo(needCapacity.add(calculateTxFee(transaction, feeRate))) < 0) {
      throw new IOException("Capacity not enough!");
    }
    return lockInputsMap;
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
    long tipBlockNumber = api.getTipBlockNumber().longValue();
    long pageNumber = 1;

    while (pageNumber <= tipBlockNumber) {
      List<LiveCell> liveCells =
          api.getLiveCellsByLockHash(
              lockHash, String.valueOf(pageNumber), String.valueOf(PAGE_SIZE), false);

      if (liveCells != null && liveCells.size() > 0) {
        for (LiveCell liveCell : liveCells) {
          capacity = capacity.add(Numeric.toBigInt(liveCell.cellOutput.capacity));
        }
      }
      pageNumber += PAGE_SIZE;
    }
    return capacity;
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
