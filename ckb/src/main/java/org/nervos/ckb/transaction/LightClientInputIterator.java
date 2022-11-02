package org.nervos.ckb.transaction;

import com.nervos.lightclient.LightClientApi;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;

public class LightClientInputIterator extends AbstractInputIterator {
  private LightClientApi lightClientApi;

  public LightClientInputIterator(
      LightClientApi lightClientApi,
      Order order,
      Integer limit) {
    this.lightClientApi = lightClientApi;
    this.order = order;
    this.limit = limit;
  }

  public LightClientInputIterator(LightClientApi api) {
    this.lightClientApi = api;
  }

  @Override
  public CellsResponse getLiveCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    return lightClientApi.getCells(searchKey, order, limit, afterCursor);
  }

  @Override
  public long getTipBlockNumber() throws IOException {
    return lightClientApi.getTipHeader().number;
  }
}
