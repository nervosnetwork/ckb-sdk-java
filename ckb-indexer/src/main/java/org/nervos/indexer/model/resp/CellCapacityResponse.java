package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class CellCapacityResponse {
  @SerializedName("block_hash")
  public String blockHash;

  @SerializedName("block_number")
  public String blockNumber;

  @SerializedName("")
  public String capacity;
}
