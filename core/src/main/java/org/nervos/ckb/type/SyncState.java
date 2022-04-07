package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

public class SyncState {
  @SerializedName("best_known_block_number")
  public int bestKnownBlockNumber;

  @SerializedName("best_known_block_timestamp")
  public long bestKnownBlockTimestamp;

  @SerializedName("fast_time")
  public long fastTime;

  public boolean ibd;

  @SerializedName("inflight_blocks_count")
  public int inflightBlocksCount;

  @SerializedName("low_time")
  public long lowTime;

  @SerializedName("normal_time")
  public long normalTime;

  @SerializedName("orphan_blocks_count")
  public int orphanBlocksCount;
}
