package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;

/** Created by duanyytop on 2019-06-20. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbLockHashs extends Response<List<String>> {
  public List<String> getLockHashs() {
    return result;
  }
}
