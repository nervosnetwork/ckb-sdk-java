package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Header {

  public String dao;
  public String hash;
  public String nonce;
  public String number;
  public String epoch;

  @SerializedName("compact_target")
  public String compactTarget;

  @SerializedName("parent_hash")
  public String parentHash;

  public String timestamp;

  @SerializedName("transactions_root")
  public String transactionsRoot;

  @SerializedName("proposals_hash")
  public String proposalsHash;

  @SerializedName("extra_hash")
  public String extraHash;

  public String version;
}
