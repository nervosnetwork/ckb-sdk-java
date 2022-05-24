package org.nervos.mercury;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.service.RpcService;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.mercury.model.common.AssetType;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.common.StructureType;
import org.nervos.mercury.model.req.Source;
import org.nervos.mercury.model.req.payload.*;
import org.nervos.mercury.model.resp.*;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;
import org.nervos.mercury.model.resp.info.MercurySyncState;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DefaultMercuryApi extends DefaultIndexerApi implements MercuryApi {
  private Gson g =
      GsonFactory.create()
          .newBuilder()
          .registerTypeAdapter(RecordResponse.class, new RecordResponse())
          .create();

  public DefaultMercuryApi(String mercuryUrl, boolean isDebug) {
    super(mercuryUrl, isDebug);
  }

  public DefaultMercuryApi(RpcService rpcService) {
    super(rpcService);
  }

  @Override
  public GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException {

    GetBalanceResponse resp =
        this.rpcService.post(
            RpcMethods.GET_BALANCE, Arrays.asList(payload), GetBalanceResponse.class, this.g);

    return resp;
  }

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

  @Override
  public TransactionCompletionResponse buildSimpleTransferTransaction(SimpleTransferPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_SIMPLE_TRANSFER_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  @Override
  public TransactionCompletionResponse buildAdjustAccountTransaction(AdjustAccountPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_ADJUST_ACCOUNT_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  @Override
  public GetTransactionInfoResponse getTransactionInfo(byte[] txHash) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_TRANSACTION_INFO,
        Arrays.asList(txHash),
        GetTransactionInfoResponse.class,
        this.g);
  }

  @Override
  public BlockInfoResponse getBlockInfo(GetBlockInfoPayload payload) throws IOException {
    return this.rpcService.post(
        RpcMethods.GET_BLOCK_INFO, Arrays.asList(payload), BlockInfoResponse.class, this.g);
  }

  @Override
  public List<byte[]> registerAddresses(List<String> normalAddresses) throws IOException {
    return this.rpcService.post(
        RpcMethods.REGISTER_ADDRESS,
        Arrays.asList(normalAddresses),
        new TypeToken<List<String>>() {
        }.getType(),
        this.g);
  }

  @Override
  public PaginationResponse<TransactionWithRichStatus> queryTransactionsWithTransactionView(
      QueryTransactionsPayload payload) throws IOException {
    payload.structureType = StructureType.NATIVE;

    return this.rpcService.post(
        RpcMethods.QUERY_TRANSACTIONS,
        Arrays.asList(payload),
        new TypeToken<PaginationResponse<TransactionWithRichStatus>>() {
        }.getType(),
        this.g);
  }

  @Override
  public PaginationResponse<TransactionInfoResponse> queryTransactionsWithTransactionInfo(
      QueryTransactionsPayload payload) throws IOException {
    payload.structureType = StructureType.DOUBLE_ENTRY;
    return this.rpcService.post(
        RpcMethods.QUERY_TRANSACTIONS,
        Arrays.asList(payload),
        new TypeToken<PaginationResponse<TransactionInfoResponse>>() {
        }.getType(),
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
  public MercurySyncState getSyncState() throws IOException {
    return this.rpcService.post(RpcMethods.GET_SYNC_STATE, Arrays.asList(), MercurySyncState.class);
  }

  @Override
  public TransactionCompletionResponse buildDaoDepositTransaction(DaoDepositPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_DEPOSIT_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  @Override
  public TransactionCompletionResponse buildDaoWithdrawTransaction(DaoWithdrawPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_WITHDRAW_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  @Override
  public TransactionCompletionResponse buildDaoClaimTransaction(DaoClaimPayload payload)
      throws IOException {
    return this.rpcService.post(
        RpcMethods.BUILD_DAO_CLAIM_TRANSACTION,
        Arrays.asList(payload),
        TransactionCompletionResponse.class,
        this.g);
  }

  @Override
  public TxView<TransactionWithRichStatus> getSpentTransactionWithTransactionView(
      GetSpentTransactionPayload payload) throws IOException {
    payload.structureType = StructureType.NATIVE;
    return this.rpcService.post(
        RpcMethods.GET_SPENT_TRANSACTION,
        Arrays.asList(payload),
        new TypeToken<TxView<TransactionWithRichStatus>>() {
        }.getType(),
        this.g);
  }

  @Override
  public TxView<TransactionInfoResponse> getSpentTransactionWithTransactionInfo(
      GetSpentTransactionPayload payload) throws IOException {
    payload.structureType = StructureType.DOUBLE_ENTRY;

    return this.rpcService.post(
        RpcMethods.GET_SPENT_TRANSACTION,
        Arrays.asList(payload),
        new TypeToken<TxView<TransactionInfoResponse>>() {
        }.getType(),
        this.g);
  }

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
