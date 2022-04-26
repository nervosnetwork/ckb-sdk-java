package org.nervos.ckb.indexer;

import org.nervos.ckb.type.CellOutput;
import org.nervos.ckb.type.OutPoint;

import java.util.List;

public class CkbIndexerCells {
  public List<Cell> objects;
  public byte[] lastCursor;

  public static class Cell {
    public int blockNumber;
    public OutPoint outPoint;
    public CellOutput output;
    public byte[] outputData;
    public int txIndex;
  }
}
