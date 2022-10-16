package org.nervos.indexer.model.resp;

import java.util.List;

public class TxsWithCells {
  public byte[] lastCursor;
  public List<TxWithCells> objects;
}
