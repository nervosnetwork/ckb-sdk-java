package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.Epoch;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbEpoch extends Response<Epoch> {
  public Epoch getEpoch() {
    return result;
  }
}
