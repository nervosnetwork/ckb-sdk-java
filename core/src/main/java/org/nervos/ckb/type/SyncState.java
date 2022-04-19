package org.nervos.ckb.type;

public class SyncState {
  public boolean ibd;
  public long bestKnownBlockNumber;
  public long bestKnownBlockTimestamp;
  public long orphanBlocksCount;
  public long inflightBlocksCount;
  public long fastTime;
  public long normalTime;
  public long lowTime;
}
