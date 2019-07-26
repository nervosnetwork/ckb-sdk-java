package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.BannedResultAddress;

/** Created by duanyytop on 2019-07-26. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbBannedResultAddresses extends Response<List<BannedResultAddress>> {

  public List<BannedResultAddress> getBannedResultAddress() {
    return result;
  }
}
