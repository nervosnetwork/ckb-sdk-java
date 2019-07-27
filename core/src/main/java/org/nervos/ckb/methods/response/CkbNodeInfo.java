package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.NodeInfo;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbNodeInfo extends Response<NodeInfo> {

  public NodeInfo getNodeInfo() {
    return result;
  }
}
