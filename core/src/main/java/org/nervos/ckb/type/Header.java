package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Header {

  public byte[] dao;
  public byte[] hash;
  public BigInteger nonce;
  public int number;
  public byte[] epoch;

  @SerializedName("compact_target")
  public long compactTarget;

  @SerializedName("parent_hash")
  public byte[] parentHash;

  public long timestamp;

  @SerializedName("transactions_root")
  public byte[] transactionsRoot;

  @SerializedName("proposals_hash")
  public byte[] proposalsHash;

  @SerializedName("extra_hash")
  public byte[] extraHash;

  public int version;
}
