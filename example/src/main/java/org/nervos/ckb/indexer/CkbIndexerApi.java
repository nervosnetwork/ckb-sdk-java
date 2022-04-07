package org.nervos.ckb.indexer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.nervos.ckb.service.RpcService;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
public class CkbIndexerApi {

  private RpcService rpcService;

  public CkbIndexerApi(String indexerUrl) {
    this(indexerUrl, false);
  }

  public CkbIndexerApi(String indexerUrl, boolean isDebug) {
    rpcService = new RpcService(indexerUrl, isDebug);
  }

  public CkbIndexerCells getCells(
      SearchKey searchKey, Order order, Integer limit, byte[] afterCursor) throws IOException {
    if (afterCursor == null || afterCursor.length == 0) {
      return rpcService.post(
          "get_cells", Arrays.asList(searchKey, order, limit), CkbIndexerCells.class);
    } else {
      return rpcService.post(
          "get_cells", Arrays.asList(searchKey, order, limit, afterCursor), CkbIndexerCells.class);
    }
  }

  public CkbIndexerCellsCapacity getCellsCapacity(SearchKey searchKey) throws IOException {
    return rpcService.post(
        "get_cells_capacity", Collections.singletonList(searchKey), CkbIndexerCellsCapacity.class);
  }
}
