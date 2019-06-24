package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-05-07. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Epoch {
  public String number;

  @JsonProperty("epoch_reward")
  public String epochReward;

  @JsonProperty("start_number")
  public String startNumber;

  public String length;
  public String difficulty;

  public Epoch() {}
}
