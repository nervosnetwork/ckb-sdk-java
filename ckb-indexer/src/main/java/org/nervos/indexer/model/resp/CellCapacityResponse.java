package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class CellCapacityResponse {
  @SerializedName("block_hash")
  public byte[] blockHash;

  @SerializedName("block_number")
  public int blockNumber;

  public BigInteger capacity;
}
