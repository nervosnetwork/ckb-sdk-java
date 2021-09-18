package org.nervos.indexer.resp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransactionResponse {

  @SerializedName("last_cursor")
  public String lastCursor;

  public List<TransactionInfoResponse> objects;

  public static class RpcTransactionResponse {

    @SerializedName("last_cursor")
    public List<Byte> lastCursor;

    public List<TransactionInfoResponse> objects;
  }
}
