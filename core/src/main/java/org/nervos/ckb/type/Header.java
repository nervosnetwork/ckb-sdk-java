package org.nervos.ckb.type;

import java.math.BigInteger;

import static org.nervos.ckb.utils.MoleculeConverter.packUint128;

public class Header {
  public byte[] dao;
  public byte[] hash;
  public BigInteger nonce;
  public long number;
  public long epoch;
  public int compactTarget;
  public byte[] parentHash;
  public long timestamp;
  public byte[] transactionsRoot;
  public byte[] proposalsHash;
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
