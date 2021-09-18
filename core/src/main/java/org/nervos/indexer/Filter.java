package org.nervos.indexer;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.nervos.ckb.type.Script;

public class Filter {
  public Script script;

  @SerializedName("output_data_len_range")
  public List<String> outputDataLenRange;

  @SerializedName("output_capacity_range")
  public List<String> outputCapacityRange;

  @SerializedName("block_range")
  public List<String> blockRange;
}
