package org.nervos.ckb.type.cell;

import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.type.OutPoint;

import java.math.BigInteger;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellInput {

  @SerializedName("previous_output")
  public OutPoint previousOutput;

  public BigInteger since;

  public CellInput() {}

  public CellInput(OutPoint previousOutput, BigInteger since) {
    this.previousOutput = previousOutput;
    this.since = since;
  }

  public CellInput(OutPoint previousOutput) {
    this(previousOutput, BigInteger.ZERO);
  }
}
