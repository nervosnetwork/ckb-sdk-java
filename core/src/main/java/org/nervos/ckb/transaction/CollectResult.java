package org.nervos.ckb.transaction;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectResult {
  public List<CellsWithAddress> cellsWithAddresses;
  public long changeCapacity;

  public CollectResult(List<CellsWithAddress> cellsWithAddresses, long changeCapacity) {
    this.cellsWithAddresses = cellsWithAddresses;
    this.changeCapacity = changeCapacity;
  }
}
