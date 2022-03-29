package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Header {

  public byte[] dao;
  public byte[] hash;
  public byte[] nonce;
  public int number;
  public byte[] epoch;

  @SerializedName("compact_target")
  public byte[] compactTarget;

  @SerializedName("parent_hash")
  public byte[] parentHash;

  public byte[] timestamp;

  @SerializedName("transactions_root")
  public byte[] transactionsRoot;

  @SerializedName("proposals_hash")
  public byte[] proposalsHash;

  @SerializedName("extra_hash")
  public byte[] extraHash;

  public int version;
}
