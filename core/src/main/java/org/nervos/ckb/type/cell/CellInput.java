package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellInput {

  @SerializedName("previous_output")
  public OutPoint previousOutput;

  public String since;

  public CellInput() {}

  public CellInput(OutPoint previousOutput, String since) {
    this.previousOutput = previousOutput;
    this.since = since;
  }
}
