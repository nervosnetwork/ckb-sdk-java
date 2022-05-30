package org.nervos.api;

import org.nervos.ckb.CkbRpcApi;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.RpcResponse;
import org.nervos.ckb.service.RpcService;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.type.*;
import org.nervos.indexer.CkbIndexerApi;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.indexer.model.resp.CellsResponse;
import org.nervos.indexer.model.resp.TipResponse;
import org.nervos.indexer.model.resp.TransactionResponse;
import org.nervos.mercury.DefaultMercuryApi;
import org.nervos.mercury.MercuryApi;
import org.nervos.mercury.model.common.PaginationResponse;
import org.nervos.mercury.model.req.payload.*;
import org.nervos.mercury.model.resp.*;
import org.nervos.mercury.model.resp.info.DBInfo;
import org.nervos.mercury.model.resp.info.MercuryInfo;
import org.nervos.mercury.model.resp.info.MercurySyncState;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DefaultCkbApi implements CkbApi {
  private CkbRpcApi ckbApi;

  private MercuryApi mercuryApi;

  private CkbIndexerApi ckbIndexerApi;

  public DefaultCkbApi(String ckbUrl, String mercuryUrl, String indexerUrl, boolean isDebug) {
    if (Objects.nonNull(ckbUrl)) {
      this.ckbApi = new Api(new RpcService(ckbUrl, isDebug));
    }

    if (Objects.nonNull(indexerUrl)) {
      this.ckbIndexerApi = new DefaultIndexerApi(new RpcService(indexerUrl, isDebug));
    }

    if (Objects.nonNull(mercuryUrl)) {
      this.mercuryApi = new DefaultMercuryApi(new RpcService(mercuryUrl, isDebug));
    }
  }

  @Override
  public TipResponse getTip() throws IOException {
    if (this.mercuryApi != null) {
      return this.mercuryApi.getTip();
    }

    if (this.ckbIndexerApi != null) {
      return this.ckbIndexerApi.getTip();
    }

    return null;
  }

  @Override
  public CellsResponse getCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor)
      throws IOException {

    if (this.mercuryApi != null) {
      //      return this.mercuryApi.getCells(searchKey, order, limit, afterCursor);
    }

    if (this.ckbIndexerApi != null) {
      return this.ckbIndexerApi.getCells(searchKey, order, limit, afterCursor);
    }

    return null;
  }

  @Override
  public TransactionResponse getTransactions(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {

    if (this.mercuryApi != null) {
      //      return this.mercuryApi.getTransactions(searchKey, order, limit, afterCursor);
    }

    if (this.ckbIndexerApi != null) {
      return this.ckbIndexerApi.getTransactions(searchKey, order, limit, afterCursor);
    }
    return null;
  }

  @Override
  public CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException {

    if (this.mercuryApi != null) {
      return this.mercuryApi.getCellsCapacity(searchKey);
    }
    if (this.ckbIndexerApi != null) {
      return this.ckbIndexerApi.getCellsCapacity(searchKey);
    }

    return null;
  }

  @Override
  public Block getBlock(byte[] blockHash) throws IOException {
    return this.ckbApi.getBlock(blockHash);
  }

  @Override
  public Block getBlockByNumber(long blockNumber) throws IOException {
    return this.ckbApi.getBlockByNumber(blockNumber);
  }

  @Override
  public TransactionWithStatus getTransaction(byte[] transactionHash) throws IOException {
    return this.ckbApi.getTransaction(transactionHash);
  }

  @Override
  public byte[] getBlockHash(long blockNumber) throws IOException {
    return this.ckbApi.getBlockHash(blockNumber);
  }

  @Override
  public BlockEconomicState getBlockEconomicState(byte[] blockHash) throws IOException {
    return this.ckbApi.getBlockEconomicState(blockHash);
  }

  @Override
  public Header getTipHeader() throws IOException {
    return this.ckbApi.getTipHeader();
  }

  @Override
  public CellWithStatus getLiveCell(OutPoint outPoint, boolean withData) throws IOException {
    return this.ckbApi.getLiveCell(outPoint, withData);
  }

  @Override
  public long getTipBlockNumber() throws IOException {
    return this.ckbApi.getTipBlockNumber();
  }

  @Override
  public Epoch getCurrentEpoch() throws IOException {
    return this.ckbApi.getCurrentEpoch();
  }

  @Override
  public Epoch getEpochByNumber(long epochNumber) throws IOException {
    return this.ckbApi.getEpochByNumber(epochNumber);
  }

  @Override
  public Header getHeader(byte[] blockHash) throws IOException {
    return this.ckbApi.getHeader(blockHash);
  }

  @Override
  public Header getHeaderByNumber(long blockNumber) throws IOException {
    return this.ckbApi.getHeaderByNumber(blockNumber);
  }

  @Override
  public TransactionProof getTransactionProof(List<byte[]> txHashes) throws IOException {
    return this.ckbApi.getTransactionProof(txHashes);
  }

  @Override
  public TransactionProof getTransactionProof(List<byte[]> txHashes, byte[] blockHash)
      throws IOException {
    return this.ckbApi.getTransactionProof(txHashes, blockHash);
  }

  @Override
  public List<byte[]> verifyTransactionProof(TransactionProof transactionProof) throws IOException {
    return this.ckbApi.verifyTransactionProof(transactionProof);
  }

  @Override
  public Block getForkBlock(byte[] blockHash) throws IOException {
    return this.ckbApi.getForkBlock(blockHash);
  }

  @Override
  public Consensus getConsensus() throws IOException {
    return this.ckbApi.getConsensus();
  }

  @Override
  public long getBlockMedianTime(byte[] blockHash) throws IOException {
    return this.ckbApi.getBlockMedianTime(blockHash);
  }

  @Override
  public BlockchainInfo getBlockchainInfo() throws IOException {
    return this.ckbApi.getBlockchainInfo();
  }

  @Override
  public TxPoolInfo txPoolInfo() throws IOException {
    return this.ckbApi.txPoolInfo();
  }

  @Override
  public void clearTxPool() throws IOException {
    this.ckbApi.clearTxPool();
  }

  @Override
  public RawTxPool getRawTxPool() throws IOException {
    return this.ckbApi.getRawTxPool();
  }

  @Override
  public RawTxPoolVerbose getRawTxPoolVerbose() throws IOException {
    return this.ckbApi.getRawTxPoolVerbose();
  }

  @Override
  public byte[] sendTransaction(Transaction transaction) throws IOException {
    return this.ckbApi.sendTransaction(transaction);
  }

  @Override
  public byte[] sendTransaction(Transaction transaction, OutputsValidator outputsValidator)
      throws IOException {
    return this.ckbApi.sendTransaction(transaction, outputsValidator);
  }

  @Override
  public NodeInfo localNodeInfo() throws IOException {
    return this.ckbApi.localNodeInfo();
  }

  @Override
  public List<PeerNodeInfo> getPeers() throws IOException {
    return this.ckbApi.getPeers();
  }

  @Override
  public SyncState syncState() throws IOException {
    return this.ckbApi.syncState();
  }

  @Override
  public void setNetworkActive(boolean state) throws IOException {
    this.ckbApi.setNetworkActive(state);
  }

  @Override
  public void addNode(String peerId, String address) throws IOException {
    this.ckbApi.addNode(peerId, address);
  }

  @Override
  public void removeNode(String peerId) throws IOException {
    this.ckbApi.removeNode(peerId);
  }

  @Override
  public void setBan(BannedAddress bannedAddress) throws IOException {
    this.ckbApi.setBan(bannedAddress);
  }

  @Override
  public List<BannedResultAddress> getBannedAddresses() throws IOException {
    return this.ckbApi.getBannedAddresses();
  }

  @Override
  public void clearBannedAddresses() throws IOException {
    this.ckbApi.clearBannedAddresses();
  }

  @Override
  public void pingPeers() throws IOException {
    this.ckbApi.pingPeers();
  }

  @Override
  public Cycles dryRunTransaction(Transaction transaction) throws IOException {
    return this.ckbApi.dryRunTransaction(transaction);
  }

  @Override
  public long calculateDaoMaximumWithdraw(OutPoint outPoint, byte[] withdrawBlockHash)
      throws IOException {
    return this.ckbApi.calculateDaoMaximumWithdraw(outPoint, withdrawBlockHash);
  }

  @Override
  public List<RpcResponse> batchRPC(List<List> requests) throws IOException {
    return this.ckbApi.batchRPC(requests);
  }

  @Override
  public GetBalanceResponse getBalance(GetBalancePayload payload) throws IOException {
    return this.mercuryApi.getBalance(payload);
  }

  @Override
  public AccountInfo getAccountInfo(AccountInfoPayload payload) throws IOException {
    return this.mercuryApi.getAccountInfo(payload);
  }

  @Override
  public TransactionWithScriptGroups buildTransferTransaction(TransferPayload payload)
      throws IOException {
    return this.mercuryApi.buildTransferTransaction(payload);
  }

  @Override
  public TransactionWithScriptGroups buildAdjustAccountTransaction(AdjustAccountPayload payload)
      throws IOException {
    return this.mercuryApi.buildAdjustAccountTransaction(payload);
  }

  @Override
  public TransactionWithScriptGroups buildSimpleTransferTransaction(SimpleTransferPayload payload)
      throws IOException {
    return this.mercuryApi.buildSimpleTransferTransaction(payload);
  }

  @Override
  public GetTransactionInfoResponse getTransactionInfo(byte[] txHash) throws IOException {
    return this.mercuryApi.getTransactionInfo(txHash);
  }

  @Override
  public BlockInfoResponse getBlockInfo(GetBlockInfoPayload payload) throws IOException {
    return this.mercuryApi.getBlockInfo(payload);
  }

  @Override
  public List<byte[]> registerAddresses(List<String> normalAddresses) throws IOException {
    return this.mercuryApi.registerAddresses(normalAddresses);
  }

  @Override
  public PaginationResponse<TransactionWithRichStatus> queryTransactionsWithTransactionView(
      QueryTransactionsPayload payload) throws IOException {
    return this.mercuryApi.queryTransactionsWithTransactionView(payload);
  }

  @Override
  public PaginationResponse<TransactionInfoResponse> queryTransactionsWithTransactionInfo(
      QueryTransactionsPayload payload) throws IOException {
    return this.mercuryApi.queryTransactionsWithTransactionInfo(payload);
  }

  @Override
  public DBInfo getDbInfo() throws IOException {
    return this.mercuryApi.getDbInfo();
  }

  @Override
  public MercuryInfo getMercuryInfo() throws IOException {
    return this.mercuryApi.getMercuryInfo();
  }

  @Override
  public MercurySyncState getSyncState() throws IOException {
    return this.mercuryApi.getSyncState();
  }

  @Override
  public TxView<TransactionWithRichStatus> getSpentTransactionWithTransactionView(
      GetSpentTransactionPayload payload) throws IOException {
    return this.mercuryApi.getSpentTransactionWithTransactionView(payload);
  }

  @Override
  public TxView<TransactionInfoResponse> getSpentTransactionWithTransactionInfo(
      GetSpentTransactionPayload payload) throws IOException {
    return this.mercuryApi.getSpentTransactionWithTransactionInfo(payload);
  }

  @Override
  public TransactionWithScriptGroups buildDaoDepositTransaction(DaoDepositPayload payload)
      throws IOException {
    return this.mercuryApi.buildDaoDepositTransaction(payload);
  }

  @Override
  public TransactionWithScriptGroups buildDaoWithdrawTransaction(DaoWithdrawPayload payload)
      throws IOException {
    return this.mercuryApi.buildDaoWithdrawTransaction(payload);
  }

  @Override
  public TransactionWithScriptGroups buildDaoClaimTransaction(DaoClaimPayload payload)
      throws IOException {
    return this.mercuryApi.buildDaoClaimTransaction(payload);
  }

  @Override
  public TransactionWithScriptGroups buildSudtIssueTransaction(SudtIssuePayload payload)
      throws IOException {
    return this.mercuryApi.buildSudtIssueTransaction(payload);
  }
}
