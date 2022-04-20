package org.nervos.ckb.type;

public class BlockEconomicState {
  public byte[] finalizedAt;
  public Issuance issuance;
  public MinerReward minerReward;
  public long txsFee;

  public static class Issuance {
    public long primary;
    public long secondary;
  }

  public static class MinerReward {
    public long committed;
    public long primary;
    public long proposal;
    public long secondary;
  }
}
