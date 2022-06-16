package org.nervos.mercury.model.resp.info;

import com.google.gson.annotations.SerializedName;

public class MercurySyncState {
  public State type;
  public SyncInfo value;

  public enum State {
    @SerializedName("ReadOnly")
    READ_ONLY,
    @SerializedName("Serial")
    SERIAL,
    @SerializedName("ParallelFirstStage")
    PARALLEL_FIRST_STAGE,
    @SerializedName("ParallelSecondStage")
    PARALLEL_SECOND_STAGE,
  }

  public static class SyncInfo {
    public long current;
    public long target;
    public String progress;
  }
}
