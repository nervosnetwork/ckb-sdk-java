package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.Cycles;

/** Created by duanyytop on 2019-05-11. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbCycles extends Response<Cycles> {

  public Cycles getCycles() {
    return result;
  }
}
