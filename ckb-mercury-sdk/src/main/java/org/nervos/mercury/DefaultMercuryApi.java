package org.nervos.mercury;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.nervos.ckb.service.RpcService;
import org.nervos.indexer.DefaultIndexerApi;
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
import org.nervos.mercury.model.req.payload.SudtIssuePayload;
import org.nervos.mercury.model.req.payload.TransferPayload;
import org.nervos.mercury.model.resp.BlockInfoResponse;
import org.nervos.mercury.model.resp.GetBalanceResponse;
import org.nervos.mercury.model.resp.GetTransactionInfoResponse;
import org.nervos.mercury.model.resp.TransactionCompletionResponse;
import org.nervos.mercury.model.resp.TransactionInfoResponse;
import org.nervos.mercury.model.resp.TransactionWithRichStatus;
import org.nervos.mercury.model.resp.TxView;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;
import org.nervos.mercury.model.resp.info.MercurySyncState;

public class DefaultMercuryApi extends DefaultIndexerApi implements MercuryApi {
  private Gson g = GsonFactory.newGson();

  public DefaultMercuryApi(String mercuryUrl, boolean isDebug) {
    super(mercuryUrl, isDebug);
  }

  public DefaultMercuryApi(RpcService rpcService) {
    super(rpcService);
  }

  // OK
  @Override
  public GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException {

    GetBalanceResponse resp =
        this.rpcService.post(
            RpcMethods.GET_BALANCE, Arrays.asList(payload), GetBalanceResponse.class, this.g);

    return resp;
  }

  // OK
  @Override
  public TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException {

    if (Objects.equals(payload.assetInfo.assetType, AssetType.CKB)
        && Objects.equals(payload.from.source, Source.CLAIMABLE)) {
      throw new RuntimeException("The transaction does not support ckb");
    }

    return this.rpcService.post(
        RpcMethods.BUILD_TRANSFER_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  // OK
  @Override
  public TransactionCompletionResponse buildSimpleTransferTransaction(SimpleTransferPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_SIMPLE_TRANSFER_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  // OK
  @Override
  public TransactionCompletionResponse buildAdjustAccountTransaction(AdjustAccountPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_ADJUST_ACCOUNT_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  // OK
  @Override
  public GetTransactionInfoResponse getTransactionInfo(byte[] txHash) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_TRANSACTION_INFO,
        Arrays.asList(txHash),
        GetTransactionInfoResponse.class,
        this.g);
  }

  // OK
  @Override
  public BlockInfoResponse getBlockInfo(GetBlockInfoPayload payload) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_BLOCK_INFO, Arrays.asList(payload), BlockInfoResponse.class, this.g);
  }

  // OK
  @Override
  public List<byte[]> registerAddresses(List<String> normalAddresses) throws IOException {
    return this.rpcService.post(
        RpcMethods.REGISTER_ADDRESS,
        Arrays.asList(normalAddresses),
        new TypeToken<List<String>>() {}.getType(),
        this.g);
  }

  // OK
  @Override
  public PaginationResponse<TxView<TransactionWithRichStatus>> queryTransactionsWithTransactionView(
      QueryTransactionsPayload payload) throws IOException {
    payload.viewType = ViewType.NATIVE;

    return this.rpcService.post(
        RpcMethods.QUERY_TRANSACTIONS,
        Arrays.asList(payload),
        new TypeToken<PaginationResponse<TxView<TransactionWithRichStatus>>>() {}.getType(),
        this.g);
  }

  // OK
  @Override
  public PaginationResponse<TxView<TransactionInfoResponse>> queryTransactionsWithTransactionInfo(
      QueryTransactionsPayload payload) throws IOException {
    payload.viewType = ViewType.DOUBLE_ENTRY;
    return this.rpcService.post(
        RpcMethods.QUERY_TRANSACTIONS,
        Arrays.asList(payload),
        new TypeToken<PaginationResponse<TxView<TransactionInfoResponse>>>() {}.getType(),
        this.g);
  }

  // OK
  @Override
  public DBInfo getDbInfo() throws IOException {
    return this.rpcService.post(RpcMethods.GET_DB_INFO, Arrays.asList(), DBInfo.class);
  }

  // OK
  @Override
  public MercuryInfo getMercuryInfo() throws IOException {
    return this.rpcService.post(RpcMethods.GET_MERCURY_INFO, Arrays.asList(), MercuryInfo.class);
  }

  @Override
  public MercurySyncState getSyncState() throws IOException {
    return this.rpcService.post(RpcMethods.GET_SYNC_STATE, Arrays.asList(), MercurySyncState.class);
  }

  // OK
  @Override
  public TransactionCompletionResponse buildDaoDepositTransaction(DaoDepositPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_DEPOSIT_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  // OK
  @Override
  public TransactionCompletionResponse buildDaoWithdrawTransaction(DaoWithdrawPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_WITHDRAW_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  // OK
  @Override
  public TransactionCompletionResponse buildDaoClaimTransaction(DaoClaimPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_CLAIM_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  // OK
  @Override
  public TxView<TransactionWithRichStatus> getSpentTransactionWithTransactionView(
      GetSpentTransactionPayload payload) throws IOException {
    payload.structureType = ViewType.NATIVE;
    return this.rpcService.post(
        RpcMethods.GET_SPENT_TRANSACTION,
        Arrays.asList(payload),
        new TypeToken<TxView<TransactionWithRichStatus>>() {}.getType(),
        this.g);
  }

  // OK
  @Override
  public TxView<TransactionInfoResponse> getSpentTransactionWithTransactionInfo(
      GetSpentTransactionPayload payload) throws IOException {
    payload.structureType = ViewType.DOUBLE_ENTRY;

    return this.rpcService.post(
        RpcMethods.GET_SPENT_TRANSACTION,
        Arrays.asList(payload),
        new TypeToken<TxView<TransactionInfoResponse>>() {}.getType(),
        this.g);
  }

  // OK
  @Override
  public TransactionCompletionResponse buildSudtIssueTransaction(SudtIssuePayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_SUDT_ISSUE_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }
}
