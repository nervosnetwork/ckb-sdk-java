package indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class Cell {

  @SerializedName("block_number")
  public String blockNumber;

  @SerializedName("out_point")
  public OutPoint outPoint;

  public Output output;

  @SerializedName("output_data")
  public String outputData;

  @SerializedName("tx_index")
  public String txIndex;
}
