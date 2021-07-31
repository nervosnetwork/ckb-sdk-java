package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class LockHashCapacity {

  public String capacity;

  @SerializedName("cells_count")
  public String cellsCount;

  @SerializedName("block_number")
  public String blockNumber;
}
