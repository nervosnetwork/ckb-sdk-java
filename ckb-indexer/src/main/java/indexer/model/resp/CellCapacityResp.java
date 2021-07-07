package indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class CellCapacityResp {
  @SerializedName("block_hash")
  public String blockHash;

  @SerializedName("block_number")
  public String blockNumber;

  @SerializedName("")
  public String capacity;
}
