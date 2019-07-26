package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.BannedAddress;

/** Created by duanyytop on 2019-07-26. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbBannedAddresses extends Response<List<BannedAddress>> {

  public List<BannedAddress> getBannedAddress() {
    return result;
  }
}
