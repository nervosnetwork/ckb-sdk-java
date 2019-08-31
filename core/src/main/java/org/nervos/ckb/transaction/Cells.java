package org.nervos.ckb.transaction;

import java.math.BigInteger;
import java.util.List;
import org.nervos.ckb.methods.type.cell.CellInput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Cells {
  List<CellInput> inputs;
  BigInteger capacity;

  Cells(List<CellInput> inputs, BigInteger capacity) {
    this.inputs = inputs;
    this.capacity = capacity;
  }
}
