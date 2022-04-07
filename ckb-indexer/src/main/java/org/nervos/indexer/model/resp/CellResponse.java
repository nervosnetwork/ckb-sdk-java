package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class CellResponse {

  @SerializedName("block_number")
  public int blockNumber;

  @SerializedName("out_point")
  public OutPointResponse outPoint;

  public OutputResponse output;

  @SerializedName("output_data")
  public byte[] outputData;

  @SerializedName("tx_index")
  public int txIndex;
}
