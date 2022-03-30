package org.nervos.indexer.model.resp;

import org.nervos.ckb.type.Script;

import java.math.BigInteger;

public class OutputResponse {
  public BigInteger capacity;
  public Script lock;
  public Script type;
}
