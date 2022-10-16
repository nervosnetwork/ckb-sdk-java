package org.nervos.indexer.model.resp;

import java.util.List;

public class TxsWithCell {
  public byte[] lastCursor;
  public List<TxWithCell> objects;
}
