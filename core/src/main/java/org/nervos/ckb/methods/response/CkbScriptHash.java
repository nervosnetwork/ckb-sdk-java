package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbScriptHash extends Response<String> {

  public String getScriptHash() {
    return result;
  }
}
