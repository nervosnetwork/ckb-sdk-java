package org.nervos.ckb.type;

public class SyncState {
  public byte[] AssumeValidTarget;
  public boolean AssumeValidTargetReached;
  public long BestKnownBlockNumber;
  public long BestKnownBlockTimestamp;
  public long FastTime;
  public boolean Ibd;
  public long InflightBlocksCount;
  public long LowTime;
  public long MinChainWork;
  public boolean MinChainWorkReached;
  public long NormalTime;
  public long OrphanBlocksCount;
  public byte[] TipHash;
  public long TipNumber;
  public byte[] UnverifiedTipHash;
  public long UnverifiedTipNumber;
}
