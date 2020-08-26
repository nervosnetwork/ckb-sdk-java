package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CkbIndexerCellResponse {
  public List<CkbIndexerCell> objects;

  @SerializedName("last_cursor")
  public String lastCursor;
}
