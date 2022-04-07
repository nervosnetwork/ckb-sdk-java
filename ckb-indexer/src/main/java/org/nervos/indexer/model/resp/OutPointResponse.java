package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;

public class OutPointResponse {
  public int index;

  @SerializedName("tx_hash")
  public byte[] txHash;
}
