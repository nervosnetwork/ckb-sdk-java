package org.nervos.ckb.transaction;

import java.util.List;
import org.nervos.ckb.type.cell.CellInput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellsWithAddress {
  public List<CellInput> inputs;
  public String address;

  public CellsWithAddress(List<CellInput> inputs, String address) {
    this.inputs = inputs;
    this.address = address;
  }
}
