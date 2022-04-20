package org.nervos.ckb.type.cell;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Utils;

import static org.nervos.ckb.utils.MoleculeConverter.packUint64;

public class CellOutput {
  public long capacity;
  public Script type;
  public Script lock;

  public CellOutput() {
  }

  public CellOutput(long capacity, Script lock) {
    this.capacity = capacity;
    this.lock = lock;
  }

  public CellOutput(long capacity, Script lock, Script type) {
    this.capacity = capacity;
    this.lock = lock;
    this.type = type;
  }

  public long occupiedCapacity(byte[] data) {
    long byteSize = Utils.ckbToShannon(8);
    if (data != null) {
      byteSize += Utils.ckbToShannon(data.length);
    }
    if (lock != null) {
      byteSize += lock.occupiedCapacity();
    }
    if (type != null) {
      byteSize += type.occupiedCapacity();
    }
    return byteSize;
  }

  public org.nervos.ckb.newtype.concrete.CellOutput pack() {
    return org.nervos.ckb.newtype.concrete.CellOutput.builder()
        .setLock(lock.pack())
        .setType(type == null ? null : type.pack())
        .setCapacity(packUint64(capacity))
        .build();
  }
}
