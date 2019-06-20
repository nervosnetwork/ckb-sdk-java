package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-06-20. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class LockHashIndexState {
  @JsonProperty("lock_hash")
  public String lockHash;

  @JsonProperty("block_number")
  public String blockNumber;

  @JsonProperty("block_hash")
  public String blockHash;
}
