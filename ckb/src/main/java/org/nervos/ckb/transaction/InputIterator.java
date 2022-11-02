package org.nervos.ckb.transaction;

import org.nervos.ckb.CkbRpcApi;
import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.utils.address.Address;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;

public class InputIterator extends AbstractInputIterator {
  private CkbRpcApi ckbRpcApi;

  public InputIterator(
      CkbRpcApi ckbRpcApi,
      Order order,
      Integer limit) {
    this.ckbRpcApi = ckbRpcApi;
    this.order = order;
    this.limit = limit;
  }

  public InputIterator(CkbRpcApi api) {
    this.ckbRpcApi = api;
  }

  public InputIterator(String address) {
    this(getDefaultCkbRpcApi(Address.decode(address).getNetwork()));
    addSearchKey(address);
  }

  private static CkbRpcApi getDefaultCkbRpcApi(Network network) {
    String url;
    switch (network) {
      case MAINNET:
        url = "https://mainnet.ckb.dev";
        break;
      case TESTNET:
        url = "https://testnet.ckb.dev";
        break;
      default:
        throw new IllegalArgumentException("Unsupported network");
    }
    return new Api(url, false);
  }

  @Override
  public CellsResponse getLiveCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    return ckbRpcApi.getCells(searchKey, order, limit, afterCursor);
  }

  @Override
  public long getTipBlockNumber() throws IOException {
    return ckbRpcApi.getTipBlockNumber();
  }
}
