package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class LockHashIndexState {
  @SerializedName("lock_hash")
  public String lockHash;

  @SerializedName("block_number")
  public String blockNumber;

  @SerializedName("block_hash")
  public String blockHash;
}
