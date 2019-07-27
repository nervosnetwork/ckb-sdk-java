package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.LockHashIndexState;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbLockHashIndexStates extends Response<List<LockHashIndexState>> {

  public List<LockHashIndexState> getLockHashIndexStates() {
    return result;
  }
}
