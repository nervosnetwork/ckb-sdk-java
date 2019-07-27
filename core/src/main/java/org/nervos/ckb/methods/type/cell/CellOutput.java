package org.nervos.ckb.methods.type.cell;

import org.nervos.ckb.methods.type.Script;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutput {
  public String capacity;
  public Script type;
  public Script lock;
  public String data;

  public CellOutput() {}

  public CellOutput(String capacity, String data, Script lock) {
    this.capacity = capacity;
    this.data = data;
    this.lock = lock;
  }
}
