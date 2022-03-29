package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class BlockEconomicState {

  @SerializedName("finalized_at")
  public byte[] finalizedAt;

  public Issuance issuance;

  @SerializedName("miner_reward")
  public MinerReward minerReward;

  @SerializedName("txs_fee")
  public BigInteger txsFee;

  public static class Issuance {
    public BigInteger primary;
    public BigInteger secondary;
  }

  public static class MinerReward {
    public BigInteger committed;
    public BigInteger primary;
    public BigInteger proposal;
    public BigInteger secondary;
  }
}
