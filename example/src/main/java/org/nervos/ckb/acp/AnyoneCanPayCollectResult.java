package org.nervos.ckb.acp;

import java.math.BigInteger;
import org.nervos.ckb.type.cell.CellInput;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class AnyoneCanPayCollectResult {

  public CellInput cellInput;
  public BigInteger capacity;

  public AnyoneCanPayCollectResult(CellInput cellInput, BigInteger capacity) {
    this.cellInput = cellInput;
    this.capacity = capacity;
  }
}
