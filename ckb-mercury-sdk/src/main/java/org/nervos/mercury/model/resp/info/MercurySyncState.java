package org.nervos.mercury.model.resp.info;

import java.math.BigInteger;

public class MercurySyncState {
  public State type;
  public SyncInfo value;

  enum State {
    ReadOnly,
    Serial,
    ParallelFirstStage,
    ParallelSecondStage
  }

  class SyncInfo {
    public BigInteger current;
    public BigInteger target;
    public String progress;
  }
}
