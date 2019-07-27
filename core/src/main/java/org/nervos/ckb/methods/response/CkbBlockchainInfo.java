package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.BlockchainInfo;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbBlockchainInfo extends Response<BlockchainInfo> {
  public BlockchainInfo getBlockchainInfo() {
    return result;
  }
}
