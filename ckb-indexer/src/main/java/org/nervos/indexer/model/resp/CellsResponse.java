package org.nervos.indexer.model.resp;

import java.util.List;

public class CellsResponse {
  public byte[] lastCursor;
  public List<CellResponse> objects;
}
