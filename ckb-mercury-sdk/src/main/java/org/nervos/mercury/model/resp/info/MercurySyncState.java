package org.nervos.mercury.model.resp.info;

import com.google.gson.annotations.SerializedName;

public class MercurySyncState {
  public State type;
  public SyncInfo value;

  enum State {
    @SerializedName("ReadOnly")
    READ_ONLY,
    @SerializedName("Serial")
    SERIAL,
    @SerializedName("ParallelFirstStage")
    PARALLEL_FIRST_STAGE,
    @SerializedName("ParallelSecondStage")
    PARALLEL_SECOND_STAGE,
  }

  class SyncInfo {
    public int current;
    public int target;
    public String progress;
  }
}
