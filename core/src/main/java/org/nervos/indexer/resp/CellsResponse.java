package org.nervos.indexer.resp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CellsResponse {
  @SerializedName("last_cursor")
  public String lastCursor;

  public List<CellResponse> objects;

  public class RpcCellsResponse {
    @SerializedName("last_cursor")
    public List<Byte> lastCursor;

    public List<CellResponse> objects;
  }
}
