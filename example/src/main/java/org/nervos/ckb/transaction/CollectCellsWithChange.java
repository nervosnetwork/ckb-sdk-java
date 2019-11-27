package org.nervos.ckb.transaction;

import java.util.List;
import java.util.Map;
import org.nervos.ckb.type.cell.CellInput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectCellsWithChange {
  public Map<String, List<CellInput>> lockInputsMap;
  public String changeCapacity;

  public CollectCellsWithChange(Map<String, List<CellInput>> lockInputsMap, String changeCapacity) {
    this.lockInputsMap = lockInputsMap;
    this.changeCapacity = changeCapacity;
  }
}
