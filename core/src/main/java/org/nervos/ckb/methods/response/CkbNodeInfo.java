package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.NodeInfo;

/** Created by duanyytop on 2018-12-21. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbNodeInfo extends Response<NodeInfo> {

  public NodeInfo getNodeInfo() {
    return result;
  }
}
