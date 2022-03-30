package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class TransactionInfoResponse {
  @SerializedName("block_number")
  public int blockNumber;

  @SerializedName("io_index")
  public int ioIndex;

  @SerializedName("io_type")
  public String ioType;

  @SerializedName("tx_hash")
  public byte[] txHash;

  @SerializedName("tx_index")
  public int txIndex;
}
