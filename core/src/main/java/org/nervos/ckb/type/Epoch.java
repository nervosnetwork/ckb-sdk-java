package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Epoch {
  public String number;

  @SerializedName("start_number")
  public String startNumber;

  public String length;
  public String difficulty;

  public Epoch() {}
}
