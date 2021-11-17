package org.nervos.mercury;

import static java.util.stream.Collectors.toList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.nervos.ckb.service.RpcService;
import org.nervos.indexer.CkbIndexerRpcMethods;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.indexer.model.resp.CellsResponse;
import org.nervos.indexer.model.resp.TipResponse;
import org.nervos.indexer.model.resp.TransactionResponse;
import org.nervos.mercury.model.common.AssetType;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.ViewType;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.payload.AdjustAccountPayload;
import org.nervos.mercury.model.req.payload.DaoClaimPayload;
import org.nervos.mercury.model.req.payload.DaoDepositPayload;
import org.nervos.mercury.model.req.payload.DaoWithdrawPayload;
import org.nervos.mercury.model.req.payload.GetBalancePayload;
import org.nervos.mercury.model.req.payload.GetBlockInfoPayload;
import org.nervos.mercury.model.req.payload.GetSpentTransactionPayload;
import org.nervos.mercury.model.req.payload.QueryTransactionsPayload;
import org.nervos.mercury.model.req.payload.SimpleTransferPayload;
import org.nervos.mercury.model.req.payload.TransferPayload;
import org.nervos.mercury.model.resp.BlockInfoResponse;
import org.nervos.mercury.model.resp.GetBalanceResponse;
import org.nervos.mercury.model.resp.GetTransactionInfoResponse;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import org.nervos.mercury.model.resp.TransactionInfo;
import org.nervos.mercury.model.resp.TransactionView;
import org.nervos.mercury.model.resp.indexer.MercuryCellsResponse;
import org.nervos.mercury.model.resp.indexer.MercuryTransactionResponse;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;

public class DefaultMercuryApi implements MercuryApi {

  private RpcService rpcService;
  private Gson g = GsonFactory.newGson();

  public DefaultMercuryApi(String mercuryUrl, boolean isDebug) {
    this.rpcService = new RpcService(mercuryUrl, isDebug);
  }

  public DefaultMercuryApi(RpcService rpcService) {
    this.rpcService = rpcService;
  }

  @Override
  public GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException {

    GetBalanceResponse resp =
        this.rpcService.post(
            RpcMethods.GET_BALANCE, Arrays.asList(payload), GetBalanceResponse.class, g);

    return resp;
  }

  @Override
  public TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException {

    if (Objects.equals(payload.assetInfo.assetType, AssetType.CKB)
        && Objects.equals(payload.from.source, Source.Claimable)) {
      throw new RuntimeException("The transaction does not support ckb");
    }

    return this.rpcService.post(
        RpcMethods.BUILD_TRANSFER_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionCompletionResponse buildSimpleTransferTransaction(SimpleTransferPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_SIMPLE_TRANSFER_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionCompletionResponse buildAdjustAccountTransaction(AdjustAccountPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_ADJUST_ACCOUNT_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public GetTransactionInfoResponse getTransactionInfo(String txHash) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_TRANSACTION_INFO,
        Arrays.asList(txHash),
        GetTransactionInfoResponse.class,
        g);
  }

  @Override
  public BlockInfoResponse getBlockInfo(GetBlockInfoPayload payload) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_BLOCK_INFO, Arrays.asList(payload), BlockInfoResponse.class, this.g);
  }

  @Override
  public List<String> registerAddress(List<String> normalAddresses) throws IOException {
    return this.rpcService.post(
        RpcMethods.REGISTER_ADDRESS,
        Arrays.asList(normalAddresses),
        new TypeToken<List<String>>() {}.getType());
  }

  @Override
  public PaginationResponse<TransactionView> queryTransactionsWithTransactionView(
      QueryTransactionsPayload payload) throws IOException {
    payload.viewType = ViewType.Native;
    return this.rpcService.post(
        RpcMethods.QUERY_TRANSACTIONS,
        Arrays.asList(payload),
        new TypeToken<PaginationResponse<TransactionView>>() {}.getType(),
        this.g);
  }

  @Override
  public PaginationResponse<TransactionInfo> queryTransactionsWithTransactionInfo(
      QueryTransactionsPayload payload) throws IOException {
    payload.viewType = ViewType.DoubleEntry;
    return this.rpcService.post(
        RpcMethods.QUERY_TRANSACTIONS,
        Arrays.asList(payload),
        new TypeToken<PaginationResponse<TransactionInfo>>() {}.getType(),
        this.g);
  }

  @Override
  public DBInfo getDbInfo() throws IOException {
    return this.rpcService.post(RpcMethods.GET_DB_INFO, Arrays.asList(), DBInfo.class);
  }

  @Override
  public MercuryInfo getMercuryInfo() throws IOException {
    return this.rpcService.post(RpcMethods.GET_MERCURY_INFO, Arrays.asList(), MercuryInfo.class);
  }

  @Override
  public TransactionCompletionResponse buildDaoDepositTransaction(DaoDepositPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_DEPOSIT_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionCompletionResponse buildDaoWithdrawTransaction(DaoWithdrawPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_WITHDRAW_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionCompletionResponse buildDaoClaimTransaction(DaoClaimPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_CLAIM_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class);
  }

  @Override
  public TransactionView getSpentTransactionWithTransactionView(GetSpentTransactionPayload payload)
      throws IOException {
    payload.structureType = ViewType.Native;
    return this.rpcService.post(
        RpcMethods.GET_SPENT_TRANSACTION, Arrays.asList(payload), TransactionView.class);
  }

  @Override
  public TransactionInfo getSpentTransactionWithTransactionInfo(GetSpentTransactionPayload payload)
      throws IOException {
    payload.structureType = ViewType.DoubleEntry;
    return this.rpcService.post(
        RpcMethods.GET_SPENT_TRANSACTION, Arrays.asList(payload), TransactionInfo.class, this.g);
  }

  @Override
  public CellsResponse getCells(SearchKey searchKey, String order, String limit, String afterCursor)
      throws IOException {

    List<Integer> mercuryCursor =
        (afterCursor == null || afterCursor == "")
            ? null
            : Arrays.stream(afterCursor.split(",")).map(x -> Integer.valueOf(x)).collect(toList());

    MercuryCellsResponse response =
        this.rpcService.post(
            CkbIndexerRpcMethods.GET_CELLS,
            Arrays.asList(searchKey, order, limit, mercuryCursor),
            MercuryCellsResponse.class);

    return response.toCellsResponse();
  }

  @Override
  public TransactionResponse getTransactions(
      SearchKey searchKey, String order, String limit, String afterCursor) throws IOException {

    List<Integer> mercuryCursor =
        (afterCursor == null || afterCursor == "")
            ? null
            : Arrays.stream(afterCursor.split(",")).map(x -> Integer.valueOf(x)).collect(toList());

    MercuryTransactionResponse response =
        this.rpcService.post(
            CkbIndexerRpcMethods.GET_TRANSACTIONS,
            Arrays.asList(searchKey, order, limit, mercuryCursor),
            MercuryTransactionResponse.class);

    return response.toTransactionResponse();
  }

  @Override
  public TipResponse getTip() throws IOException {
    return this.rpcService.post(CkbIndexerRpcMethods.GET_TIP, Arrays.asList(), TipResponse.class);
  }

  @Override
  public CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException {
    return this.rpcService.post(
        CkbIndexerRpcMethods.GET_CELLS_CAPACITY,
        Arrays.asList(searchKey),
        CellCapacityResponse.class);
  }
}
