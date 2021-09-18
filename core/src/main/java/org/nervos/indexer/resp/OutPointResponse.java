package org.nervos.indexer.resp;

import com.google.gson.annotations.SerializedName;

public class OutPointResponse {
  public String index;

  @SerializedName("tx_hash")
  public String txHash;
}
