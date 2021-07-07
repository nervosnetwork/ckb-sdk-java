package indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class OutPoint {
  public String index;

  @SerializedName("tx_hash")
  public String txHash;
}
