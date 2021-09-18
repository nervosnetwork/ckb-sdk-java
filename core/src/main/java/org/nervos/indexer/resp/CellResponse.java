package org.nervos.indexer.resp;

import com.google.gson.annotations.SerializedName;

public class CellResponse {

  @SerializedName("block_number")
  public String blockNumber;

  @SerializedName("out_point")
  public OutPointResponse outPoint;

  public OutputResponse output;

  @SerializedName("output_data")
  public String outputData;

  @SerializedName("tx_index")
  public String txIndex;
}
