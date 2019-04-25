package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Created by duanyytop on 2018-12-20. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Block {
  public Header header;

  @JsonProperty("commit_transactions")
  public List<Transaction> commitTransactions;

  @JsonProperty("proposal_transactions")
  public List<Byte[]> proposalTransactions; // Fixed 10-element array representing short hash.

  public List<Uncle> uncles;

  public static class Uncle {

    public Header header;

    @JsonProperty("proposal_transactions")
    public List<Byte[]> proposalTransactions;
  }
}
