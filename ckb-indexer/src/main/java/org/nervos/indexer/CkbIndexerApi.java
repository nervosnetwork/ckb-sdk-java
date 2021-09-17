package org.nervos.indexer;

import java.io.IOException;
import org.nervos.indexer.resp.CellCapacityResponse;
import org.nervos.indexer.resp.CellsResponse;
import org.nervos.indexer.resp.TipResponse;
import org.nervos.indexer.resp.TransactionResponse;

public interface CkbIndexerApi {
  TipResponse getTip() throws IOException;

  CellsResponse getCells(SearchKey searchKey, String order, String limit, String afterCursor)
      throws IOException;

  TransactionResponse getTransactions(
      SearchKey searchKey, String order, String limit, String afterCursor) throws IOException;

  CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException;
}
