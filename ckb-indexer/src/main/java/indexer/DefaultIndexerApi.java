package indexer;

import com.google.gson.Gson;
import indexer.model.SearchKey;
import indexer.model.resp.CellCapacityResp;
import indexer.model.resp.CellsResp;
import indexer.model.resp.TipResp;
import indexer.model.resp.TransactionResp;
import org.nervos.ckb.service.RpcService;

import java.io.IOException;
import java.util.Arrays;

public class DefaultIndexerApi implements CkbIndexerApi {

  protected RpcService rpcService;

  private Gson gson = new Gson();

  public DefaultIndexerApi(String mercuryUrl, boolean isDebug) {
    this.rpcService = new RpcService(mercuryUrl, isDebug);
  }

  @Override
  public TipResp getTip() throws IOException {
    return this.rpcService.post(CkbIndexerRpcMethods.GET_TIP, Arrays.asList(), TipResp.class);
  }

  @Override
  public CellsResp getCells(SearchKey searchKey, String order, String limit, String afterCursor)
      throws IOException {
    return this.rpcService.post(
        CkbIndexerRpcMethods.GET_CELLS,
        Arrays.asList(searchKey, order, limit, afterCursor),
        CellsResp.class);
  }

  @Override
  public TransactionResp getTransactions(
      SearchKey searchKey, String order, String limit, String afterCursor) throws IOException {
    return this.rpcService.post(
        CkbIndexerRpcMethods.GET_TRANSACTIONS,
        Arrays.asList(searchKey, order, limit, afterCursor),
        TransactionResp.class);
  }

  @Override
  public CellCapacityResp getCellsCapacity(SearchKey searchKey) throws IOException {
    return this.rpcService.post(
        CkbIndexerRpcMethods.GET_CELLS_CAPACITY, Arrays.asList(searchKey), CellCapacityResp.class);
  }
}
