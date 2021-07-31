package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2021 Nervos Foundation. All rights reserved. */
public class Consensus {
  @SerializedName("block_version")
  public String blockVersion;

  @SerializedName("cellbase_maturity")
  public String cellbaseMaturity;

  @SerializedName("dao_type_hash")
  public String daoTypeHash;

  @SerializedName("epoch_duration_target")
  public String epochDurationTarget;

  @SerializedName("genesis_hash")
  public String genesisHash;

  public String id;

  @SerializedName("initial_primary_epoch_reward")
  public String initialPrimaryEpochReward;

  @SerializedName("max_block_bytes")
  public String maxBlockBytes;

  @SerializedName("max_block_cycles")
  public String maxBlockCycles;

  @SerializedName("max_block_proposals_limit")
  public String maxBlockProposalsLimit;

  @SerializedName("max_uncles_num")
  public String maxUnclesNum;

  @SerializedName("median_time_block_count")
  public String medianTimeBlockCount;

  @SerializedName("orphan_rate_target")
  public Ratio orphanRateTarget;

  @SerializedName("permanent_difficulty_in_dummy")
  public boolean permanentDifficultyInDummy;

  @SerializedName("primary_epoch_reward_halving_interval")
  public String primaryEpochRewardHalvingInterval;

  @SerializedName("proposer_reward_ratio")
  public Ratio proposerRewardRatio;

  @SerializedName("secondary_epoch_reward")
  public String secondaryEpochReward;

  @SerializedName("secp256k1_blake160_multisig_all_type_hash")
  public String secp256k1Blake160MultisigAllTypeHash;

  @SerializedName("secp256k1_blake160_sighash_all_type_hash")
  public String secp256k1Blake160SighashAllTypeHash;

  @SerializedName("tx_proposal_window")
  public TxProposalWindow txProposalWindow;

  @SerializedName("tx_version")
  public String txVersion;

  @SerializedName("type_id_code_hash")
  public String typeIdCodeHash;

  public static class Ratio {
    public String denom;
    public String numer;
  }

  public static class TxProposalWindow {
    public String closest;
    public String farthest;
  }
}
