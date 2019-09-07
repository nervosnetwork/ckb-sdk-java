package org.nervos.ckb.methods.type.cell;

import org.nervos.ckb.methods.type.Script;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutput {
  public String capacity;
  public Script type;
  public Script lock;

  public CellOutput() {}

  public CellOutput(String capacity, Script lock) {
    this.capacity = capacity;
    this.lock = lock;
  }

  public CellOutput(String capacity, Script lock, Script type) {
    this.capacity = capacity;
    this.lock = lock;
    this.type = type;
  }
}
