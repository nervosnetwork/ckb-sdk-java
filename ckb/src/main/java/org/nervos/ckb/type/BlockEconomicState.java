package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class BlockEconomicState {

  @SerializedName("finalized_at")
  public String finalizedAt;

  @SerializedName("proposal_reward")
  public String proposalReward;

  public Issuance issuance;

  @SerializedName("miner_reward")
  public MinerReward minerReward;

  @SerializedName("txs_fee")
  public String txsFee;

  public static class Issuance {
    public String primary;
    public String secondary;
  }

  public static class MinerReward {
    public String committed;
    public String primary;
    public String proposal;
    public String secondary;
  }
}
