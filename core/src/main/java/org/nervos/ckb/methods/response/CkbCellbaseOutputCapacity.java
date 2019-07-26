package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.CellbaseOutputCapacity;

/** Created by duanyytop on 2019-07-26. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbCellbaseOutputCapacity extends Response<CellbaseOutputCapacity> {

  public CellbaseOutputCapacity getCellbaseOutputCapacity() {
    return result;
  }
}
