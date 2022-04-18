package org.nervos.indexer.model.resp;

import org.nervos.ckb.type.Script;

public class OutputResponse {
  public long capacity;
  public Script lock;
  public Script type;
}
