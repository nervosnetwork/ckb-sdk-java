package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.address.CodeHashType;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellCollector {

  private Api api;

  public CellCollector(Api api) {
    this.api = api;
  }

  public CollectedCells getCellInputs(String lockHash, BigInteger needCapacity, BigInteger fee)
      throws IOException {
    List<CellInput> cellInputs = new ArrayList<>();
    BigInteger inputsCapacity = BigInteger.ZERO;
    List witnesses = new ArrayList();
    long toBlockNumber = api.getTipBlockNumber().longValue();
    long fromBlockNumber = 1;
    BigInteger sumNeedCapacity = needCapacity.add(fee);

    while (fromBlockNumber <= toBlockNumber && inputsCapacity.compareTo(sumNeedCapacity) < 0) {
      long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
      List<CellOutputWithOutPoint> cellOutputs =
          api.getCellsByLockHash(
              lockHash,
              BigInteger.valueOf(fromBlockNumber).toString(),
              BigInteger.valueOf(currentToBlockNumber).toString());

      if (cellOutputs != null && cellOutputs.size() > 0) {
        for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputs) {
          CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0x0");
          inputsCapacity = inputsCapacity.add(Numeric.toBigInt(cellOutputWithOutPoint.capacity));
          cellInputs.add(cellInput);
          if (inputsCapacity.compareTo(sumNeedCapacity) > 0) {
            break;
          }
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    witnesses.add(new Witness(Witness.EMPTY_LOCK));
    if (inputsCapacity.compareTo(sumNeedCapacity) < 0) {
      throw new IOException("Capacity not enough!");
    }

    return new CollectedCells(cellInputs, inputsCapacity, witnesses);
  }

  public CollectedCells getCellInputs(String lockHash, BigInteger needCapacity) throws IOException {
    return getCellInputs(lockHash, needCapacity, BigInteger.ZERO);
  }

  public BigInteger getCapacityWithAddress(String address) throws IOException {
    return getCapacityWithAddress(address, CodeHashType.BLAKE160);
  }

  public BigInteger getCapacityWithAddress(String address, CodeHashType codeHashType)
      throws IOException {
    SystemScriptCell systemScriptCell;
    if (codeHashType == CodeHashType.BLAKE160) {
      systemScriptCell = SystemContract.getSystemSecpCell(api);
    } else {
      systemScriptCell = SystemContract.getSystemMultiSigCell(api);
    }
    Script lockScript =
        Utils.generateLockScriptWithAddress(address, systemScriptCell.cellHash, codeHashType);
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
