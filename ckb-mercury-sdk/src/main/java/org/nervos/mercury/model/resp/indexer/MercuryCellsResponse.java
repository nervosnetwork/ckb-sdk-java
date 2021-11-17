package org.nervos.mercury.model.resp.indexer;

import static java.util.stream.Collectors.toList;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.nervos.indexer.model.resp.CellResponse;
import org.nervos.indexer.model.resp.CellsResponse;

public class MercuryCellsResponse {
  @SerializedName("last_cursor")
  public List<Integer> cursor;

  public List<CellResponse> objects;

  public CellsResponse toCellsResponse() {
    CellsResponse cellsResponse = new CellsResponse();
    cellsResponse.objects = this.objects;
    cellsResponse.lastCursor =
        this.cursor == null
            ? null
            : String.join(",", this.cursor.stream().map(x -> x.toString()).collect(toList()));

    return cellsResponse;
  }
}
