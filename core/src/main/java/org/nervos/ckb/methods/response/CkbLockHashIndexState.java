package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.LockHashIndexState;

/** Created by duanyytop on 2019-06-20. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbLockHashIndexState extends Response<LockHashIndexState> {

  public LockHashIndexState getLockHashIndexState() {
    return result;
  }
}
