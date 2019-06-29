package org.nervos.ckb.methods.type.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nervos.ckb.methods.type.OutPoint;

/** Created by duanyytop on 2019-01-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellInput {

  @JsonProperty("previous_output")
  public OutPoint previousOutput;

  public String since;

  public CellInput() {}

  public CellInput(OutPoint previousOutput, String since) {
    this.previousOutput = previousOutput;
    this.since = since;
  }
}
