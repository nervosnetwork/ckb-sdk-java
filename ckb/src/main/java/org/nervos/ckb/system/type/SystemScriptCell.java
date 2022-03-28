package org.nervos.ckb.system.type;

import org.nervos.ckb.type.OutPoint;

public class SystemScriptCell {

  public byte[] cellHash;
  public OutPoint outPoint;

  public SystemScriptCell(byte[] cellHash, OutPoint outPoint) {
    this.cellHash = cellHash;
    this.outPoint = outPoint;
  }
}
