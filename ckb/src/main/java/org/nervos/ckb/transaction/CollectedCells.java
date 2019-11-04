package org.nervos.ckb.transaction;

import java.math.BigInteger;
import java.util.List;
import org.nervos.ckb.type.cell.CellInput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CollectedCells {
  public List<CellInput> inputs;
  public BigInteger capacity;
  public List witnesses;

  CollectedCells(List<CellInput> inputs, BigInteger capacity) {
    this.inputs = inputs;
    this.capacity = capacity;
  }

  CollectedCells(List<CellInput> inputs, BigInteger capacity, List witnesses) {
    this.inputs = inputs;
    this.capacity = capacity;
    this.witnesses = witnesses;
  }
}
