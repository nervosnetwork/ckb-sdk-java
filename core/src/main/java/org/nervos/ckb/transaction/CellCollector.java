package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.type.SystemScriptCell;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellCollector {

  private Api api;

  public CellCollector(Api api) {
    this.api = api;
  }

  public CollectedCells getCellInputs(String lockHash, BigInteger needCapacity) throws IOException {
    List<CellInput> cellInputs = new ArrayList<>();
    BigInteger inputsCapacity = BigInteger.ZERO;
    long toBlockNumber = api.getTipBlockNumber().longValue();
    long fromBlockNumber = 1;

    while (fromBlockNumber <= toBlockNumber && inputsCapacity.compareTo(needCapacity) < 0) {
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
          if (inputsCapacity.compareTo(needCapacity) > 0) {
            break;
          }
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    return new CollectedCells(cellInputs, inputsCapacity);
  }

  public BigInteger getCapacityWithAddress(String address) throws IOException {
    SystemScriptCell systemScriptCell = Utils.getSystemScriptCell(api);
    Script lockScript = Utils.generateLockScriptWithAddress(address, systemScriptCell.cellHash);
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
