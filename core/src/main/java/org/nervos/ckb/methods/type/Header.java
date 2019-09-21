package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Header {

  public String dao;
  public String difficulty;
  public String hash;
  public String nonce;
  public String number;
  public String epoch;

  @JsonProperty("parent_hash")
  public String parentHash;

  public String timestamp;

  @JsonProperty("transactions_root")
  public String transactionsRoot;

  @JsonProperty("proposals_hash")
  public String proposalsHash;

  @JsonProperty("witnesses_root")
  public String witnessesRoot;

  @JsonProperty("uncles_count")
  public String unclesCount;

  @JsonProperty("uncles_hash")
  public String unclesHash;

  public String version;
}
