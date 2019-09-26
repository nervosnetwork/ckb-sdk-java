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
public class CellGatherer {

  private Api api;

  public CellGatherer(Api api) {
    this.api = api;
  }

  public CollectedCells getCellInputs(String lockHash, BigInteger needCapacities)
      throws IOException {
    List<CellInput> cellInputs = new ArrayList<>();
    BigInteger inputsCapacities = BigInteger.ZERO;
    long toBlockNumber = api.getTipBlockNumber().longValue();
    long fromBlockNumber = 1;

    while (fromBlockNumber <= toBlockNumber && inputsCapacities.compareTo(needCapacities) < 0) {
      long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
      List<CellOutputWithOutPoint> cellOutputs =
          api.getCellsByLockHash(
              lockHash,
              BigInteger.valueOf(fromBlockNumber).toString(),
              BigInteger.valueOf(currentToBlockNumber).toString());

      if (cellOutputs != null && cellOutputs.size() > 0) {
        for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputs) {
          CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0x0");
          inputsCapacities =
              inputsCapacities.add(Numeric.toBigInt(cellOutputWithOutPoint.capacity));
          cellInputs.add(cellInput);
          if (inputsCapacities.compareTo(needCapacities) > 0) {
            break;
          }
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    return new CollectedCells(cellInputs, inputsCapacities);
  }

  public BigInteger getCapacitiesWithAddress(String address) throws IOException {
    SystemScriptCell systemScriptCell = Utils.getSystemScriptCell(api);
    Script lockScript = Utils.generateLockScriptWithAddress(address, systemScriptCell.cellHash);
    return getCapacitiesWithLockHash(lockScript.computeHash());
  }

  public BigInteger getCapacitiesWithLockHash(String lockHash) {
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
