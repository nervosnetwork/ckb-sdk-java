package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class CellCapacityResponse {
  @SerializedName("block_hash")
  public byte[] blockHash;

  @SerializedName("block_number")
  public int blockNumber;

  public long capacity;
}
