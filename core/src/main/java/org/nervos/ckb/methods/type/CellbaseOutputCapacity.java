package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-07-26. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellbaseOutputCapacity {
  public String primary;

  @JsonProperty("proposal_reward")
  public String proposalReward;

  public String secondary;
  public String total;

  @JsonProperty("tx_fee")
  public String txFee;
}
