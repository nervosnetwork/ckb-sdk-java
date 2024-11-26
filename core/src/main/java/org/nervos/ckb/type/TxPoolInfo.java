package org.nervos.ckb.type;

public class TxPoolInfo {
  public long orphan;
  public long pending;
  public long proposed;
  public long lastTxsUpdatedAt;
  public long totalTxCycles;
  public long totalTxSize;
  public long minFeeRate;
  public byte[] tipHash;
  public long tipNumber;
  public long maxTxPoolSize;
  public long minRbfRate;
  public long txSizeLimit;
  public long verifyQueueSize;
}
