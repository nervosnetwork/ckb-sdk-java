package org.nervos.indexer;

import java.io.IOException;

import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.indexer.model.resp.CellsResponse;
import org.nervos.indexer.model.resp.TipResponse;
import org.nervos.indexer.model.resp.TransactionResponse;

public interface CkbIndexerApi {
  TipResponse getTip() throws IOException;

  CellsResponse getCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor)
      throws IOException;

  TransactionResponse getTransactions(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException;

  CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException;
}
