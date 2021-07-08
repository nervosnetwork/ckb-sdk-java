package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;

public class CkbIndexerTip {

  @SerializedName("block_hash")
  public String blockHash;

  @SerializedName("block_number")
  public String blockNumber;
}
