package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import org.nervos.ckb.address.AddressUtils;
import org.nervos.ckb.address.CodeHashType;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Calculator;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellCollector {

  private Api api;

  public CellCollector(Api api) {
    this.api = api;
  }

  public Map<String, List<CellInput>> collectInputs(
      List<String> lockHashes, List<CellOutput> cellOutputs, BigInteger feeRate)
      throws IOException {
    List<String> cellOutputsData = new ArrayList<>();
    for (int i = 0; i < cellOutputs.size() - 1; i++) {
      int size = Serializer.serializeCellOutput(cellOutputs.get(i)).getLength();
      if (BigInteger.valueOf(size).compareTo(Numeric.toBigInt(cellOutputs.get(i).capacity)) > 0) {
        throw new IOException("Cell output serialize size must not be bigger than capacity");
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

    BigInteger transactionFee = calculateTxFee(transaction, feeRate);

    BigInteger inputsCapacity = BigInteger.ZERO;
    List<CellInput> cellInputs = new ArrayList<>();
    Map<String, List<CellInput>> lockInputsMap = new HashMap<>();
    for (String lockHash : lockHashes) {
      lockInputsMap.put(lockHash, new ArrayList<>());
    }
    List witnesses = new ArrayList<>();

    BigInteger needCapacity = BigInteger.ZERO;
    for (CellOutput cellOutput : cellOutputs) {
      needCapacity = needCapacity.add(Numeric.toBigInt(cellOutput.capacity));
    }
    List<CellOutputWithOutPoint> cellOutputList = new ArrayList<>();
    for (int index = 0; index < lockHashes.size(); index++) {
      long toBlockNumber = api.getTipBlockNumber().longValue();
      long fromBlockNumber = 1;

      while (fromBlockNumber <= toBlockNumber
          && inputsCapacity.compareTo(needCapacity.add(transactionFee)) < 0) {
        long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
        cellOutputList.addAll(
            api.getCellsByLockHash(
                lockHashes.get(index),
                BigInteger.valueOf(fromBlockNumber).toString(),
                BigInteger.valueOf(currentToBlockNumber).toString()));
        if (cellOutputList.size() > 0) {
          for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputList) {
            CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0x0");
            inputsCapacity = inputsCapacity.add(Numeric.toBigInt(cellOutputWithOutPoint.capacity));
            List<CellInput> cellInputList = lockInputsMap.get(lockHashes.get(index));
            cellInputList.add(cellInput);
            cellInputs.add(cellInput);
            witnesses.add(new Witness(Witness.EMPTY_LOCK));
            transaction.inputs = cellInputs;
            transaction.witnesses = witnesses;
            transactionFee = calculateTxFee(transaction, feeRate);
            if (inputsCapacity.compareTo(needCapacity.add(transactionFee)) > 0) {
              cellOutputs.get(cellOutputs.size() - 1).capacity =
                  Numeric.prependHexPrefix(
                      inputsCapacity.subtract(needCapacity).subtract(transactionFee).toString(16));
              break;
            }
          }
        }
        fromBlockNumber = currentToBlockNumber + 1;
      }
    }
    if (inputsCapacity.compareTo(needCapacity.add(transactionFee)) < 0) {
      throw new IOException("Capacity not enough!");
    }
    return lockInputsMap;
  }

  private BigInteger calculateTxFee(Transaction transaction, BigInteger feeRate) {
    int txSize = Serializer.serializeTransaction(transaction).toBytes().length;
    return Calculator.calculateTransactionFee(BigInteger.valueOf(txSize), feeRate);
  }

  public BigInteger getCapacityWithAddress(String address) throws IOException {
    CodeHashType codeHashType = AddressUtils.parseAddressType(address);
    SystemScriptCell systemScriptCell;
    if (codeHashType == CodeHashType.BLAKE160) {
      systemScriptCell = SystemContract.getSystemSecpCell(api);
    } else {
      systemScriptCell = SystemContract.getSystemMultiSigCell(api);
    }
    Script lockScript = LockUtils.generateLockScriptWithAddress(address, systemScriptCell.cellHash);
    return getCapacityWithLockHash(lockScript.computeHash());
  }

  public BigInteger getCapacityWithLockHash(String lockHash) throws IOException {
    BigInteger capacity = BigInteger.ZERO;
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
          capacity = capacity.add(Numeric.toBigInt(output.capacity));
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    return capacity;
  }
}
