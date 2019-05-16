package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.CellOutPoint;

public class CkbSystemContract extends Response<CkbSystemContract> {
  public String systemScriptCellHash;
  public CellOutPoint systemScriptOutPoint;

  public CkbSystemContract(String systemSriptCellHash, CellOutPoint systemScriptOutPoint) {
    this.systemScriptCellHash = systemSriptCellHash;
    this.systemScriptOutPoint = systemScriptOutPoint;
  }
}
