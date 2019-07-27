package org.nervos.ckb.methods.response;

import org.nervos.ckb.methods.Response;
import org.nervos.ckb.methods.type.Block;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CkbBlock extends Response<Block> {

  public Block getBlock() {
    return result;
  }
}
