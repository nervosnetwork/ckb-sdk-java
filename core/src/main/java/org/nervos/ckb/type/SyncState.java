package org.nervos.ckb.type;

public class SyncState {
  public byte[] assumeValidTarget;
  public boolean assumeValidTargetReached;
  public long bestKnownBlockNumber;
  public long bestKnownBlockTimestamp;
  public long fastTime;
  public boolean ibd;
  public long inflightBlocksCount;
  public long lowTime;
  public long minChainWork;
  public boolean minChainWorkReached;
  public long normalTime;
  public long orphanBlocksCount;
  public byte[] tipHash;
  public long tipNumber;
  public byte[] unverifiedTipHash;
  public long unverifiedTipNumber;
}
