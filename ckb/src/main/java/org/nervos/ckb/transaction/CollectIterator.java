package org.nervos.ckb.transaction;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public interface CollectIterator<CellInput, BigInteger, String> {
  boolean hasNext(CellInput input, BigInteger capacity, String lockHash);
}
