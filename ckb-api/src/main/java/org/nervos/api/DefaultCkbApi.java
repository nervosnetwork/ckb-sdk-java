package org.nervos.api;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import org.nervos.ckb.CkbRpcApi;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.RpcResponse;
import org.nervos.ckb.service.RpcService;
import org.nervos.ckb.type.BannedAddress;
import org.nervos.ckb.type.BannedResultAddress;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.BlockEconomicState;
import org.nervos.ckb.type.BlockchainInfo;
import org.nervos.ckb.type.Consensus;
import org.nervos.ckb.type.Cycles;
import org.nervos.ckb.type.Epoch;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.NodeInfo;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.PeerNodeInfo;
import org.nervos.ckb.type.RawTxPool;
import org.nervos.ckb.type.RawTxPoolVerbose;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.SyncState;
import org.nervos.ckb.type.TransactionProof;
import org.nervos.ckb.type.TxPoolInfo;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.param.OutputsValidator;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.type.transaction.TransactionWithStatus;
import org.nervos.indexer.CkbIndexerApi;
import org.nervos.indexer.DefaultIndexerApi;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.indexer.model.resp.CellsResponse;
import org.nervos.indexer.model.resp.TipResponse;
import org.nervos.indexer.model.resp.TransactionResponse;
import org.nervos.mercury.DefaultMercuryApi;
import org.nervos.mercury.MercuryApi;
import org.nervos.mercury.model.common.PaginationResponse;
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

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
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
  public CellsResponse getCells(SearchKey searchKey, String order, String limit, String afterCursor)
      throws IOException {

    if (this.mercuryApi != null) {
      return this.mercuryApi.getCells(searchKey, order, limit, afterCursor);
    }

    if (this.ckbIndexerApi != null) {
      return this.ckbIndexerApi.getCells(searchKey, order, limit, afterCursor);
    }

    return null;
  }

  @Override
  public TransactionResponse getTransactions(
      SearchKey searchKey, String order, String limit, String afterCursor) throws IOException {

    if (this.mercuryApi != null) {
      return this.mercuryApi.getTransactions(searchKey, order, limit, afterCursor);
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
  public Block getBlockByNumber(int blockNumber) throws IOException {
    return this.ckbApi.getBlockByNumber(blockNumber);
  }

  @Override
  public TransactionWithStatus getTransaction(byte[] transactionHash) throws IOException {
    return this.ckbApi.getTransaction(transactionHash);
  }

  @Override
  public byte[] getBlockHash(int blockNumber) throws IOException {
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
  public BigInteger getTipBlockNumber() throws IOException {
    return this.ckbApi.getTipBlockNumber();
  }

  @Override
  public Epoch getCurrentEpoch() throws IOException {
    return this.ckbApi.getCurrentEpoch();
  }

  @Override
  public Epoch getEpochByNumber(int epochNumber) throws IOException {
    return this.ckbApi.getEpochByNumber(epochNumber);
  }

  @Override
  public Header getHeader(byte[] blockHash) throws IOException {
    return this.ckbApi.getHeader(blockHash);
  }

  @Override
  public Header getHeaderByNumber(int blockNumber) throws IOException {
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
  public Long getBlockMedianTime(byte[] blockHash) throws IOException {
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
  public void setNetworkActive(Boolean state) throws IOException {
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
  public BigInteger calculateDaoMaximumWithdraw(OutPoint outPoint, String withdrawBlockHash)
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
  public TransactionCompletionResponse buildTransferTransaction(TransferPayload payload)
      throws IOException {
    return this.mercuryApi.buildTransferTransaction(payload);
  }

  @Override
  public TransactionCompletionResponse buildAdjustAccountTransaction(AdjustAccountPayload payload)
      throws IOException {
    return this.mercuryApi.buildAdjustAccountTransaction(payload);
  }

  @Override
  public TransactionCompletionResponse buildSimpleTransferTransaction(SimpleTransferPayload payload)
      throws IOException {
    return this.mercuryApi.buildSimpleTransferTransaction(payload);
  }

  @Override
  public GetTransactionInfoResponse getTransactionInfo(String txHash) throws IOException {
    return this.mercuryApi.getTransactionInfo(txHash);
  }

  @Override
  public BlockInfoResponse getBlockInfo(GetBlockInfoPayload payload) throws IOException {
    return this.mercuryApi.getBlockInfo(payload);
  }

  @Override
  public List<String> registerAddresses(List<String> normalAddresses) throws IOException {
    return this.mercuryApi.registerAddresses(normalAddresses);
  }

  @Override
  public PaginationResponse<TxView<TransactionWithRichStatus>> queryTransactionsWithTransactionView(
      QueryTransactionsPayload payload) throws IOException {
    return this.mercuryApi.queryTransactionsWithTransactionView(payload);
  }

  @Override
  public PaginationResponse<TxView<TransactionInfoResponse>> queryTransactionsWithTransactionInfo(
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
  public TransactionCompletionResponse buildDaoDepositTransaction(DaoDepositPayload payload)
      throws IOException {
    return this.mercuryApi.buildDaoDepositTransaction(payload);
  }

  @Override
  public TransactionCompletionResponse buildDaoWithdrawTransaction(DaoWithdrawPayload payload)
      throws IOException {
    return this.mercuryApi.buildDaoWithdrawTransaction(payload);
  }

  @Override
  public TransactionCompletionResponse buildDaoClaimTransaction(DaoClaimPayload payload)
      throws IOException {
    return this.mercuryApi.buildDaoClaimTransaction(payload);
  }

  @Override
  public TransactionCompletionResponse buildSudtIssueTransaction(SudtIssuePayload payload)
      throws IOException {
    return this.mercuryApi.buildSudtIssueTransaction(payload);
  }
}
