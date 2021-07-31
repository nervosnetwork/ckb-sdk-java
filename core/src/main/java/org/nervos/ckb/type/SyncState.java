package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

public class SyncState {
  @SerializedName("best_known_block_number")
  public String bestKnownBlockNumber;

  @SerializedName("best_known_block_timestamp")
  public String bestKnownBlockTimestamp;

  @SerializedName("fast_time")
  public String fastTime;

  public String ibd;

  @SerializedName("inflight_blocks_count")
  public String inflightBlocksCount;

  @SerializedName("low_time")
  public String lowTime;

  @SerializedName("normal_time")
  public String normalTime;

  @SerializedName("orphan_blocks_count")
  public String orphanBlocksCount;
}
