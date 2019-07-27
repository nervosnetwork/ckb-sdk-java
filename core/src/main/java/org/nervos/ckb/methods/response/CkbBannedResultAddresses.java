package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.BannedResultAddress;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbBannedResultAddresses extends Response<List<BannedResultAddress>> {

  public List<BannedResultAddress> getBannedResultAddresses() {
    return result;
  }
}
