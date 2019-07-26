package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;

/** Created by duanyytop on 2019-07-26. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbBannedResult extends Response<String> {

  public String getBanResult() {
    return result;
  }
}
