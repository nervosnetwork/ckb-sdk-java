package org.nervos.ckb.type;

import java.math.BigInteger;
import java.util.List;

public class Consensus {
  public int blockVersion;
  public long cellbaseMaturity;
  public byte[] daoTypeHash;
  public long epochDurationTarget;
  public byte[] genesisHash;
  public String id;
  public long initialPrimaryEpochReward;
  public long maxBlockBytes;
  public long maxBlockCycles;
  public long maxBlockProposalsLimit;
  public long maxUnclesNum;
  public long medianTimeBlockCount;
  public Ratio orphanRateTarget;
  public boolean permanentDifficultyInDummy;
  public long primaryEpochRewardHalvingInterval;
  public Ratio proposerRewardRatio;
  public long secondaryEpochReward;
  public byte[] secp256k1Blake160MultisigAllTypeHash;
  public byte[] secp256k1Blake160SighashAllTypeHash;
  public TxProposalWindow txProposalWindow;
  public int txVersion;
  public byte[] typeIdCodeHash;
  public List<HardForkFeature> hardForkFeatures;

  public static class Ratio {
    public BigInteger denom;
    public BigInteger numer;
  }

  public static class TxProposalWindow {
    public long closest;
    public long farthest;
  }

  public static class HardForkFeature {
    public String id;
    public long epochNumber;
  }

}
