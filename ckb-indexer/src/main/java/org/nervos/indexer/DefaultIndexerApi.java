package org.nervos.indexer;

import org.nervos.ckb.service.RpcService;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.*;

import java.io.IOException;
import java.util.Arrays;

public class DefaultIndexerApi implements CkbIndexerApi {

  protected RpcService rpcService;

  public DefaultIndexerApi(String indexerUrl, boolean isDebug) {
    this.rpcService = new RpcService(indexerUrl, isDebug);
  }

  public DefaultIndexerApi(RpcService rpcService) {
    this.rpcService = rpcService;
  }

  @Override
  public TipResponse getTip() throws IOException {
    return this.rpcService.post(CkbIndexerRpcMethods.GET_TIP, Arrays.asList(), TipResponse.class);
  }

  @Override
  public CellsResponse getCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor)
      throws IOException {
    return this.rpcService.post(
        CkbIndexerRpcMethods.GET_CELLS,
        Arrays.asList(searchKey, order, limit, afterCursor),
        CellsResponse.class);
  }

  @Override
  public TxsWithCell getTransactions(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    searchKey.groupByTransaction = false;
    return this.rpcService.post(
        CkbIndexerRpcMethods.GET_TRANSACTIONS,
        Arrays.asList(searchKey, order, limit, afterCursor),
        TxsWithCell.class);
  }

  @Override
  public TxsWithCells getTransactionsGrouped(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    searchKey.groupByTransaction = true;
    return this.rpcService.post(
        CkbIndexerRpcMethods.GET_TRANSACTIONS,
        Arrays.asList(searchKey, order, limit, afterCursor),
        TxsWithCells.class);
  }

  @Override
  public CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException {
    return this.rpcService.post(
        CkbIndexerRpcMethods.GET_CELLS_CAPACITY,
        Arrays.asList(searchKey),
        CellCapacityResponse.class);
  }
}
