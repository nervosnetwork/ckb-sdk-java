package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Epoch {
  public int number;

  @SerializedName("start_number")
  public int startNumber;

  public int length;

  @SerializedName("compact_target")
  public byte[] compactTarget;

  public Epoch() {}
}
