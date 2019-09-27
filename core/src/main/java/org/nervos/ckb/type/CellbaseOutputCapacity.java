package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellbaseOutputCapacity {
  public String primary;

  @SerializedName("proposal_reward")
  public String proposalReward;

  public String secondary;
  public String total;

  @SerializedName("tx_fee")
  public String txFee;
}
