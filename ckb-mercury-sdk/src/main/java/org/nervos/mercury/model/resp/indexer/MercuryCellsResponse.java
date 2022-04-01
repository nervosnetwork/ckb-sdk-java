package org.nervos.mercury.model.resp.indexer;

import com.google.gson.annotations.SerializedName;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.util.List;

public class MercuryCellsResponse {
  @SerializedName("last_cursor")
  public List<Integer> cursor;

  public List<CellResponse> objects;

  public CellsResponse toCellsResponse() {
    CellsResponse cellsResponse = new CellsResponse();
    cellsResponse.objects = this.objects;
    if (cursor != null) {
      cellsResponse.lastCursor = new byte[cursor.size()];
      for (int i = 0; i < cursor.size(); i++) {
        cellsResponse.lastCursor[i] = (byte) (cursor.get(i) & 0xff);
      }
    }
    return cellsResponse;
  }
}
