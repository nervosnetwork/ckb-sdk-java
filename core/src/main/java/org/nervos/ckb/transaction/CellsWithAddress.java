package org.nervos.ckb.transaction;

import org.nervos.ckb.type.CellInput;

import java.util.List;

public class CellsWithAddress {
  public List<CellInput> inputs;
  public String address;

  public CellsWithAddress(List<CellInput> inputs, String address) {
    this.inputs = inputs;
    this.address = address;
  }
}
