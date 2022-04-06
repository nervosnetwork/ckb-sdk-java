package org.nervos.ckb.type.cell;

import java.math.BigInteger;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Utils;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellOutput {
  public BigInteger capacity;
  public Script type;
  public Script lock;

  public CellOutput() {}

  public CellOutput(BigInteger capacity, Script lock) {
    this.capacity = capacity;
    this.lock = lock;
  }

  public CellOutput(BigInteger capacity, Script lock, Script type) {
    this.capacity = capacity;
    this.lock = lock;
    this.type = type;
  }

  public BigInteger occupiedCapacity(byte[] data) {
    BigInteger byteSize = Utils.ckbToShannon(8);
    if (data != null) {
      byteSize = byteSize.add(Utils.ckbToShannon(data.length));
    }
    if (lock != null) {
      byteSize = byteSize.add(lock.occupiedCapacity());
    }
    if (type != null) {
      byteSize = byteSize.add(type.occupiedCapacity());
    }
    return byteSize;
  }
}
