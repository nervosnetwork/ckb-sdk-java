package indexer.model.resp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CellsResp {
  @SerializedName("last_cursor")
  public String lastCursor;

  public List<Cell> objects;
}
