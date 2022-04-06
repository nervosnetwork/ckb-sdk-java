package org.nervos.indexer.model.resp;

import java.math.BigInteger;
import org.nervos.ckb.type.Script;

public class OutputResponse {
  public BigInteger capacity;
  public Script lock;
  public Script type;
}
