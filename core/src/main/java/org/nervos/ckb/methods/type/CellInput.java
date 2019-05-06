package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Created by duanyytop on 2019-01-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class CellInput {

  @JsonProperty("previous_output")
  public PreviousOutput previousOutput;

  public List<String> args;

  public String since;

  public CellInput() {}

  public CellInput(PreviousOutput previousOutput, List<String> args, String since) {
    this.previousOutput = previousOutput;
    this.args = args;
    this.since = since;
  }

  public static class PreviousOutput {
    @JsonProperty("tx_hash")
    public String txHash;

    public long index;

    public PreviousOutput() {}

    public PreviousOutput(String txHash, long index) {
      this.txHash = txHash;
      this.index = index;
    }
  }
}
