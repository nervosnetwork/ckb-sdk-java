package org.nervos.ckb.transaction;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectResult {
  public List<CellsWithAddress> cellsWithAddresses;
  public String changeCapacity;

  public CollectResult(List<CellsWithAddress> cellsWithAddresses, String changeCapacity) {
    this.cellsWithAddresses = cellsWithAddresses;
    this.changeCapacity = changeCapacity;
  }
}
