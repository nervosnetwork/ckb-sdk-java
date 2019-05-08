package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2018-12-21. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Header {

  public String difficulty;
  public String hash;
  public String number;
  public String epoch;

  @JsonProperty("parent_hash")
  public String parentHash;

  public String timestamp;

  @JsonProperty("transactions_root")
  public String transactionsRoot;

  @JsonProperty("proposals_root")
  public String proposalsRoot;

  @JsonProperty("witnesses_root")
  public String witnessesRoot;

  @JsonProperty("uncles_count")
  public int unclesCount;

  @JsonProperty("uncles_hash")
  public String unclesHash;

  public int version;

  public Seal seal;

  public static class Seal {
    public String nonce;

    public String proof;
  }
}
