package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.Epoch;

/** Created by duanyytop on 2019-05-07. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbEpoch extends Response<Epoch> {
  public Epoch getEpoch() {
    return result;
  }
}
