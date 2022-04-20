package org.nervos.indexer.model.resp;

public class CellResponse {
  public int blockNumber;
  public OutPointResponse outPoint;
  public OutputResponse output;
  public byte[] outputData;
  public int txIndex;
}
