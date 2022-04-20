package org.nervos.ckb.transaction;

import java.util.List;

public class CollectResult {
  public List<CellsWithAddress> cellsWithAddresses;
  public long changeCapacity;

  public CollectResult(List<CellsWithAddress> cellsWithAddresses, long changeCapacity) {
    this.cellsWithAddresses = cellsWithAddresses;
    this.changeCapacity = changeCapacity;
  }
}
