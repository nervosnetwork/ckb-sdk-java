package org.nervos.ckb.system.type;

import org.nervos.ckb.methods.type.OutPoint;

public class SystemScriptCell {

  public String cellHash;
  public OutPoint outPoint;

  public SystemScriptCell(String cellHash, OutPoint outPoint) {
    this.cellHash = cellHash;
    this.outPoint = outPoint;
  }
}
