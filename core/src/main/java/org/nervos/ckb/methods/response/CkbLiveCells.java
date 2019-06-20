package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.cell.LiveCell;

/** Created by duanyytop on 2019-06-20. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbLiveCells extends Response<List<LiveCell>> {

  public List<LiveCell> getLiveCells() {
    return result;
  }
}
