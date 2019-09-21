package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.cell.CellWithStatus;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbCellWithStatus extends Response<CellWithStatus> {

  public CellWithStatus getCellWithStatus() {
    return result;
  }
}
