package org.nervos.ckb.system.type;

import org.nervos.ckb.methods.type.CellOutPoint;

public class CkbSystemContract {

  public String systemScriptCellHash;
  public CellOutPoint systemScriptOutPoint;

  public CkbSystemContract(String systemScriptCellHash, CellOutPoint systemScriptOutPoint) {
    this.systemScriptCellHash = systemScriptCellHash;
    this.systemScriptOutPoint = systemScriptOutPoint;
  }
}
