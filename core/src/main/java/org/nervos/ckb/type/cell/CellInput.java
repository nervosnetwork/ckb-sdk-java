package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellInput {

  @SerializedName("previous_output")
  public OutPoint previousOutput;

  public byte[] since;

  public CellInput() {}

  public CellInput(OutPoint previousOutput, byte[] since) {
    this.previousOutput = previousOutput;
    this.since = since;
  }

  public CellInput(OutPoint previousOutput) {
    this(previousOutput, new byte[]{0});
  }
}
