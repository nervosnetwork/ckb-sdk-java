package org.nervos.ckb.transaction;

import java.util.List;
import org.nervos.ckb.type.cell.CellInput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectedCell {
  public List<CellInput> inputs;
  public String lockHash;

  CollectedCell(List<CellInput> inputs, String lockHash) {
    this.inputs = inputs;
    this.lockHash = lockHash;
  }
}
