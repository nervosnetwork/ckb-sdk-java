package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Epoch {
  public String number;

  @JsonProperty("start_number")
  public String startNumber;

  public String length;
  public String difficulty;

  public Epoch() {}
}
