package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.cell.CellOutput;

public class CkbIndexerCells {
  public List<Cell> objects;

  @SerializedName("last_cursor")
  public byte[] lastCursor;

  public static class Cell {
    @SerializedName("block_number")
    public int blockNumber;

    @SerializedName("out_point")
    public OutPoint outPoint;

    public CellOutput output;

    @SerializedName("output_data")
    public byte[] outputData;

    @SerializedName("tx_index")
    public int txIndex;
  }
}
