package org.nervos.indexer.model.resp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransactionResponse {

  @SerializedName("last_cursor")
  public byte[] lastCursor;

  public List<TransactionInfoResponse> objects;
}
