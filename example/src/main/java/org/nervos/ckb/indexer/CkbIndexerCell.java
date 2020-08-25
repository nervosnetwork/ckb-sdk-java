package org.nervos.ckb.indexer;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.cell.CellOutput;

public class CkbIndexerCell {
  @SerializedName("block_number")
  public String blockNumber;
  @SerializedName("out_point")
  public OutPoint outPoint;
  public CellOutput output;
  @SerializedName("output_data")
  public String outputData;
  @SerializedName("tx_index")
  public String txIndex;
}
