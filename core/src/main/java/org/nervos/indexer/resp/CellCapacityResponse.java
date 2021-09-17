package org.nervos.indexer.resp;

import com.google.gson.annotations.SerializedName;

public class CellCapacityResponse {
  @SerializedName("block_hash")
  public String blockHash;

  @SerializedName("block_number")
  public String blockNumber;

  @SerializedName("capacity")
  public String capacity;
}
