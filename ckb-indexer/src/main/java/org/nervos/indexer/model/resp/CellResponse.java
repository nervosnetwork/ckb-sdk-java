package org.nervos.indexer.model.resp;

import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.OutPoint;

public class CellResponse {
  public int blockNumber;
  public OutPoint outPoint;
  public CellOutput output;
  public byte[] outputData;
  public int txIndex;
}
