package org.nervos.indexer.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.nervos.ckb.type.Script;

public class Filter {
  public Script script;

  @SerializedName("output_data_len_range")
  public List<Integer> outputDataLenRange;

  @SerializedName("output_capacity_range")
  public List<Long> outputCapacityRange;

  @SerializedName("block_range")
  public List<Integer> blockRange;
}
