package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.cell.LiveCell;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbLiveCells extends Response<List<LiveCell>> {

  public List<LiveCell> getLiveCells() {
    return result;
  }
}
