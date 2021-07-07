package indexer;

import indexer.model.SearchKey;
import indexer.model.resp.CellCapacityResp;
import indexer.model.resp.CellsResp;
import indexer.model.resp.TipResp;
import indexer.model.resp.TransactionResp;

import java.io.IOException;

public interface CkbIndexerApi {
  TipResp getTip() throws IOException;

  CellsResp getCells(SearchKey searchKey, String order, String limit, String afterCursor)
      throws IOException;

  TransactionResp getTransactions(
      SearchKey searchKey, String order, String limit, String afterCursor) throws IOException;

  CellCapacityResp getCellsCapacity(SearchKey searchKey) throws IOException;
}
