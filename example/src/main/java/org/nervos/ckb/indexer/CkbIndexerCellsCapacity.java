package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class CkbIndexerCellsCapacity {
  @SerializedName("block_hash")
  public byte[] blockHash;

  @SerializedName("block_number")
  public int blockNumber;

  public BigInteger capacity;
}
