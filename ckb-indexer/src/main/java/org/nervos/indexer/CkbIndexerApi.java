package org.nervos.indexer;

import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.*;

import java.io.IOException;

public interface CkbIndexerApi {
  TipResponse getTip() throws IOException;

  CellsResponse getCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor)
      throws IOException;

  TransactionResponse getTransactions(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException;

  TxsWithCells getTransactionsGrouped(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException;

  CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException;
}
