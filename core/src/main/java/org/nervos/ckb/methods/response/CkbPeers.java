package org.nervos.ckb.methods.response;

import java.util.List;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.NodeInfo;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbPeers extends Response<List<NodeInfo>> {

  public List<NodeInfo> getPeers() {
    return result;
  }
}
