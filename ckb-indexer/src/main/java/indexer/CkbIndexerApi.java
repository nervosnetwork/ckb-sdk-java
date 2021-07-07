package indexer;

import java.io.IOException;

import indexer.model.SearchKey;
import indexer.model.resp.CellCapacityResponse;
import indexer.model.resp.CellsResponse;
import indexer.model.resp.TipResponse;
import indexer.model.resp.TransactionResponse;

public interface CkbIndexerApi {
  TipResponse getTip() throws IOException;

  CellsResponse getCells(SearchKey searchKey, String order, String limit, String afterCursor)
      throws IOException;

  TransactionResponse getTransactions(
      SearchKey searchKey, String order, String limit, String afterCursor) throws IOException;

  CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException;
}
