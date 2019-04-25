package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;

/** Created by duanyytop on 2018-12-21. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbBlockHash extends Response<String> {

  public String getBlockHash() {
    return result;
  }
}
