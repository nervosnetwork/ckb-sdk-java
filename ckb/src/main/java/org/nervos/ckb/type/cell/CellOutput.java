package org.nervos.ckb.type.cell;

import java.math.BigInteger;
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

  public BigInteger occupiedCapacity(String data) {
    BigInteger UNIT = BigInteger.TEN.pow(8);
    BigInteger byteSize = BigInteger.valueOf(8).multiply(UNIT);
    if (!Strings.isEmpty(data)) {
      byteSize =
          byteSize.add(
              BigInteger.valueOf(Numeric.hexStringToByteArray(data).length).multiply(UNIT));
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
