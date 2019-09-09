package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.system.type.SystemScriptCell;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellGatherer {

  private CKBService ckbService;

  public CellGatherer(CKBService ckbService) {
    this.ckbService = ckbService;
  }

  Cells getCellInputs(String lockHash, BigInteger needCapacities) throws IOException {
    List<CellInput> cellInputs = new ArrayList<>();
    BigInteger inputsCapacities = BigInteger.ZERO;
    long toBlockNumber = ckbService.getTipBlockNumber().send().getBlockNumber().longValue();
    long fromBlockNumber = 1;

    while (fromBlockNumber <= toBlockNumber && inputsCapacities.compareTo(needCapacities) < 0) {
      long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
      List<CellOutputWithOutPoint> cellOutputs =
          ckbService
              .getCellsByLockHash(
                  lockHash, String.valueOf(fromBlockNumber), String.valueOf(currentToBlockNumber))
              .send()
              .getCells();

      if (cellOutputs != null && cellOutputs.size() > 0) {
        for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputs) {
          CellInput cellInput = new CellInput(cellOutputWithOutPoint.outPoint, "0");
          inputsCapacities =
              inputsCapacities.add(new BigDecimal(cellOutputWithOutPoint.capacity).toBigInteger());
          cellInputs.add(cellInput);
          if (inputsCapacities.compareTo(needCapacities) > 0) {
            break;
          }
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    return new Cells(cellInputs, new BigDecimal(inputsCapacities).toBigInteger());
  }

  public BigInteger getCapacitiesWithPrivateKey(String privateKey) throws IOException {
    SystemScriptCell systemScriptCell = Utils.getSystemScriptCell(ckbService);
    Script lockScript = Utils.generateLockScript(privateKey, systemScriptCell.cellHash);
    return getCapacitiesWithLockHash(lockScript.computeHash());
  }

  public BigInteger getCapacitiesWithLockHash(String lockHash) throws IOException {
    BigInteger capacity = BigInteger.ZERO;
    long toBlockNumber = ckbService.getTipBlockNumber().send().getBlockNumber().longValue();
    long fromBlockNumber = 1;

    while (fromBlockNumber <= toBlockNumber) {
      long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
      List<CellOutputWithOutPoint> cellOutputs =
          ckbService
              .getCellsByLockHash(
                  lockHash, String.valueOf(fromBlockNumber), String.valueOf(currentToBlockNumber))
              .send()
              .getCells();

      if (cellOutputs != null && cellOutputs.size() > 0) {
        for (CellOutputWithOutPoint output : cellOutputs) {
          capacity = capacity.add(new BigInteger(output.capacity));
        }
      }
      fromBlockNumber = currentToBlockNumber + 1;
    }
    return capacity;
  }
}
