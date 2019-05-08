package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-05-07. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Epoch {
  public String number;

  @JsonProperty("block_reward")
  public String blockReward;

  @JsonProperty("last_block_hash_in_previous_epoch")
  public String lastBlockHashInPreviousEpoch;

  @JsonProperty("start_number")
  public String startNumber;

  public String length;
  public String difficulty;

  @JsonProperty("remainder_reward")
  public String remainderReward;

  public Epoch() {}
}
