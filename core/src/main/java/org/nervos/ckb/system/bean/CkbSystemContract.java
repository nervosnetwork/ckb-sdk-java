package org.nervos.ckb.system.bean;

import org.nervos.ckb.methods.type.CellOutPoint;

public class CkbSystemContract {
  public String systemScriptCellHash;
  public CellOutPoint systemScriptOutPoint;

  public CkbSystemContract(String systemSriptCellHash, CellOutPoint systemScriptOutPoint) {
    this.systemScriptCellHash = systemSriptCellHash;
    this.systemScriptOutPoint = systemScriptOutPoint;
  }
}
