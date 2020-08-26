package org.nervos.ckb.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.nervos.ckb.indexer.CkbIndexerCellResponse;
import org.nervos.ckb.indexer.CkbIndexerCellsCapacityResponse;
import org.nervos.ckb.indexer.SearchKey;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CkbIndexerApi {

  private RpcService rpcService;

  public CkbIndexerApi(String nodeUrl) {
    this(nodeUrl, false);
  }

  public CkbIndexerApi(String nodeUrl, boolean isDebug) {
    rpcService = new RpcService(nodeUrl, isDebug);
  }

  public CkbIndexerCellResponse getCells(
      SearchKey searchKey, String order, BigInteger limit, String afterCursor) throws IOException {
    if ("0x".equals(afterCursor)) {
      return rpcService.post(
          "get_cells",
          Arrays.asList(searchKey, order, Numeric.toHexStringWithPrefix(limit)),
          CkbIndexerCellResponse.class);
    } else {
      return rpcService.post(
          "get_cells",
          Arrays.asList(searchKey, order, Numeric.toHexStringWithPrefix(limit), afterCursor),
          CkbIndexerCellResponse.class);
    }
  }

  public CkbIndexerCellsCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException {
    return rpcService.post(
        "get_cells_capacity",
        Collections.singletonList(searchKey),
        CkbIndexerCellsCapacityResponse.class);
  }
}
