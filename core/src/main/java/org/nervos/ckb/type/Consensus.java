package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

/** Copyright © 2021 Nervos Foundation. All rights reserved. */
public class Consensus {
  @SerializedName("block_version")
  public int blockVersion;

  @SerializedName("cellbase_maturity")
  public byte[] cellbaseMaturity;

  @SerializedName("dao_type_hash")
  public byte[] daoTypeHash;

  @SerializedName("epoch_duration_target")
  public byte[] epochDurationTarget;

  @SerializedName("genesis_hash")
  public byte[] genesisHash;

  public String id;

  @SerializedName("initial_primary_epoch_reward")
  public BigInteger initialPrimaryEpochReward;

  @SerializedName("max_block_bytes")
  public int maxBlockBytes;

  @SerializedName("max_block_cycles")
  public long maxBlockCycles;

  @SerializedName("max_block_proposals_limit")
  public int maxBlockProposalsLimit;

  @SerializedName("max_uncles_num")
  public int maxUnclesNum;

  @SerializedName("median_time_block_count")
  public int medianTimeBlockCount;

  @SerializedName("orphan_rate_target")
  public Ratio orphanRateTarget;

  @SerializedName("permanent_difficulty_in_dummy")
  public boolean permanentDifficultyInDummy;

  @SerializedName("primary_epoch_reward_halving_interval")
  public int primaryEpochRewardHalvingInterval;

  @SerializedName("proposer_reward_ratio")
  public Ratio proposerRewardRatio;

  @SerializedName("secondary_epoch_reward")
  public BigInteger secondaryEpochReward;

  @SerializedName("secp256k1_blake160_multisig_all_type_hash")
  public byte[] secp256k1Blake160MultisigAllTypeHash;

  @SerializedName("secp256k1_blake160_sighash_all_type_hash")
  public byte[] secp256k1Blake160SighashAllTypeHash;

  @SerializedName("tx_proposal_window")
  public TxProposalWindow txProposalWindow;

  @SerializedName("tx_version")
  public int txVersion;

  @SerializedName("type_id_code_hash")
  public byte[] typeIdCodeHash;

  public static class Ratio {
    public int denom;
    public int numer;
  }

  public static class TxProposalWindow {
    public int closest;
    public int farthest;
  }
}
