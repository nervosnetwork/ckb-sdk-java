package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.cell.Cell;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbCell extends Response<Cell> {

  public Cell getCell() {
    return result;
  }
}
