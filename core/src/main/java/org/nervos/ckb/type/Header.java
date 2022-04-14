package org.nervos.ckb.type;

import static org.nervos.ckb.utils.MoleculeConverter.packUint128;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

public class Header {

  public byte[] dao;
  public byte[] hash;
  public BigInteger nonce;
  public int number;
  public BigInteger epoch;

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

  public RawHeader getRawHeader() {
    RawHeader rawHeader = new RawHeader();
    rawHeader.dao = dao;
    rawHeader.hash = hash;
    rawHeader.number = number;
    rawHeader.epoch = epoch;
    rawHeader.compactTarget = compactTarget;
    rawHeader.parentHash = parentHash;
    rawHeader.timestamp = timestamp;
    rawHeader.transactionsRoot = transactionsRoot;
    rawHeader.proposalsHash = proposalsHash;
    rawHeader.extraHash = extraHash;
    rawHeader.version = version;
    return rawHeader;
  }

  public org.nervos.ckb.newtype.concrete.Header pack() {
    return org.nervos.ckb.newtype.concrete.Header.builder()
        .setRaw(getRawHeader().pack())
        .setNonce(packUint128(nonce))
        .build();
  }
}
