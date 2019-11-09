package org.nervos.ckb.type.cell;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Strings;

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

  public int calculateByteSize(String data) {
    int byteSize = 8;
    if (!Strings.isEmpty(data)) {
      byteSize += Numeric.hexStringToByteArray(data).length / 2;
    }
    if (lock != null) {
      byteSize += lock.calculateByteSize();
    }
    if (type != null) {
      byteSize += type.calculateByteSize();
    }
    return byteSize;
  }
}
