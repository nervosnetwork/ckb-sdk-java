package org.nervos.ckb.type;

import java.math.BigInteger;

import static org.nervos.ckb.utils.MoleculeConverter.*;

public class RawHeader {
  public byte[] dao;
  public byte[] hash;
  public int number;
  public BigInteger epoch;
  public long compactTarget;
  public byte[] parentHash;
  public long timestamp;
  public byte[] transactionsRoot;
  public byte[] proposalsHash;
  public byte[] extraHash;
  public int version;

  public org.nervos.ckb.newtype.concrete.RawHeader pack() {
    org.nervos.ckb.newtype.concrete.RawHeader header = org.nervos.ckb.newtype.concrete.RawHeader.builder()
        .setVersion(packUint32(version))
        .setCompactTarget(packUint32(compactTarget))
        .setTimestamp(packUint64(timestamp))
        .setNumber(packUint64(number))
        .setEpoch(packUint64(epoch))
        .setParentHash(packByte32(parentHash))
        .setTransactionsRoot(packByte32(transactionsRoot))
        .setProposalsHash(packByte32(proposalsHash))
        .setExtraHash(packByte32(extraHash))
        .setDao(packByte32(dao))
        .build();
    return header;
  }
}
