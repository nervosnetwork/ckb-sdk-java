package org.nervos.ckb.system.type;

import org.nervos.ckb.methods.type.cell.CellOutPoint;

public class SystemScriptCell {

  public String cellHash;
  public CellOutPoint outPoint;

  public SystemScriptCell(String cellHash, CellOutPoint outPoint) {
    this.cellHash = cellHash;
    this.outPoint = outPoint;
  }
}
