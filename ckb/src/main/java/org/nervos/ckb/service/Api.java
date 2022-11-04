package org.nervos.ckb.service;

import com.google.gson.reflect.TypeToken;
import org.nervos.ckb.CkbRpcApi;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Convert;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Api implements CkbRpcApi {

  private RpcService rpcService;

  public Api(String nodeUrl) {
    this(nodeUrl, false);
  }

  public Api(String nodeUrl, boolean isDebug) {
    rpcService = new RpcService(nodeUrl, isDebug);
  }

  public Api(RpcService rpcService) {
    this.rpcService = rpcService;
  }

  @Override
  public Block getBlock(byte[] blockHash) throws IOException {
    return rpcService.post("get_block", Collections.singletonList(blockHash), Block.class);
  }

  @Override
  public Block getBlockByNumber(long blockNumber) throws IOException {
    return rpcService.post(
        "get_block_by_number", Collections.singletonList(blockNumber), Block.class);
  }

  @Override
  public TransactionWithStatus getTransaction(byte[] transactionHash) throws IOException {
    return rpcService.post(
        "get_transaction", Collections.singletonList(transactionHash), TransactionWithStatus.class);
  }

  @Override
  public byte[] getBlockHash(long blockNumber) throws IOException {
    return rpcService.post("get_block_hash", Collections.singletonList(blockNumber), byte[].class);
  }

  @Override
  public BlockEconomicState getBlockEconomicState(byte[] blockHash) throws IOException {
    return rpcService.post(
        "get_block_economic_state", Collections.singletonList(blockHash), BlockEconomicState.class);
  }

  @Override
  public Header getTipHeader() throws IOException {
    return rpcService.post("get_tip_header", Collections.<String>emptyList(), Header.class);
  }

  @Override
  public CellWithStatus getLiveCell(OutPoint outPoint, boolean withData) throws IOException {
    return rpcService.post(
        "get_live_cell",
        Arrays.asList(Convert.parseOutPoint(outPoint), withData),
        CellWithStatus.class);
  }

  @Override
  public long getTipBlockNumber() throws IOException {
    return rpcService.post(
        "get_tip_block_number", Collections.<String>emptyList(), Long.class);
  }

  @Override
  public Epoch getCurrentEpoch() throws IOException {
    return rpcService.post("get_current_epoch", Collections.<String>emptyList(), Epoch.class);
  }

  @Override
  public Epoch getEpochByNumber(long epochNumber) throws IOException {
    return rpcService.post(
        "get_epoch_by_number", Collections.singletonList(epochNumber), Epoch.class);
  }

  @Override
  public Header getHeader(byte[] blockHash) throws IOException {
    return rpcService.post("get_header", Collections.singletonList(blockHash), Header.class);
  }

  @Override
  public Header getHeaderByNumber(long blockNumber) throws IOException {
    return rpcService.post(
        "get_header_by_number", Collections.singletonList(blockNumber), Header.class);
  }

  @Override
  public TransactionProof getTransactionProof(List<byte[]> txHashes) throws IOException {
    return rpcService.post(
        "get_transaction_proof", Collections.singletonList(txHashes), TransactionProof.class);
  }

  @Override
  public TransactionProof getTransactionProof(List<byte[]> txHashes, byte[] blockHash)
      throws IOException {
    return rpcService.post(
        "get_transaction_proof", Arrays.asList(txHashes, blockHash), TransactionProof.class);
  }

  @Override
  public List<byte[]> verifyTransactionProof(TransactionProof transactionProof) throws IOException {
    return rpcService.post(
        "verify_transaction_proof",
        Collections.singletonList(transactionProof),
        new TypeToken<List<byte[]>>() {}.getType());
  }

  @Override
  public Block getForkBlock(byte[] blockHash) throws IOException {
    return rpcService.post("get_fork_block", Collections.singletonList(blockHash), Block.class);
  }

  @Override
  public Consensus getConsensus() throws IOException {
    return rpcService.post("get_consensus", Collections.emptyList(), Consensus.class);
  }

  @Override
  public long getBlockMedianTime(byte[] blockHash) throws IOException {
    return rpcService.post("get_block_median_time", Arrays.asList(blockHash), Long.class);
  }

  /** Stats RPC */
  @Override
  public BlockchainInfo getBlockchainInfo() throws IOException {
    return rpcService.post("get_blockchain_info", Collections.emptyList(), BlockchainInfo.class);
  }

  /** Pool RPC */
  @Override
  public TxPoolInfo txPoolInfo() throws IOException {
    return rpcService.post("tx_pool_info", Collections.emptyList(), TxPoolInfo.class);
  }

  @Override
  public void clearTxPool() throws IOException {
    rpcService.post("clear_tx_pool", Collections.emptyList(), Object.class);
  }

  @Override
  public RawTxPool getRawTxPool() throws IOException {
    return rpcService.post("get_raw_tx_pool", Collections.emptyList(), RawTxPool.class);
  }

  @Override
  public RawTxPoolVerbose getRawTxPoolVerbose() throws IOException {
    return rpcService.post(
        "get_raw_tx_pool", Collections.singletonList(true), RawTxPoolVerbose.class);
  }

  @Override
  public byte[] sendTransaction(Transaction transaction) throws IOException {
    return rpcService.post(
        "send_transaction",
        Arrays.asList(Convert.parseTransaction(transaction), OutputsValidator.PASSTHROUGH),
        byte[].class);
  }

  @Override
  public byte[] sendTransaction(Transaction transaction, OutputsValidator outputsValidator)
      throws IOException {
    return rpcService.post(
        "send_transaction",
        Arrays.asList(Convert.parseTransaction(transaction), outputsValidator),
        byte[].class);
  }

  /** Net RPC */
  @Override
  public NodeInfo localNodeInfo() throws IOException {
    return rpcService.post("local_node_info", Collections.emptyList(), NodeInfo.class);
  }

  @Override
  public List<PeerNodeInfo> getPeers() throws IOException {
    return rpcService.post(
        "get_peers", Collections.emptyList(), new TypeToken<List<PeerNodeInfo>>() {}.getType());
  }

  @Override
  public SyncState syncState() throws IOException {
    return rpcService.post("sync_state", Collections.emptyList(), SyncState.class);
  }

  @Override
  public void setNetworkActive(boolean state) throws IOException {
    rpcService.post("set_network_active", Collections.singletonList(state), Object.class);
  }

  @Override
  public void addNode(String peerId, String address) throws IOException {
    rpcService.post("add_node", Arrays.asList(peerId, address), Object.class);
  }

  @Override
  public void removeNode(String peerId) throws IOException {
    rpcService.post("remove_node", Collections.singletonList(peerId), Object.class);
  }

  @Override
  public void setBan(BannedAddress bannedAddress) throws IOException {
    rpcService.post(
        "set_ban",
        Arrays.asList(
            bannedAddress.address,
            bannedAddress.command,
            bannedAddress.banTime,
            bannedAddress.absolute,
            bannedAddress.reason),
        Object.class);
  }

  @Override
  public List<BannedResultAddress> getBannedAddresses() throws IOException {
    return rpcService.post(
        "get_banned_addresses",
        Collections.emptyList(),
        new TypeToken<List<BannedResultAddress>>() {}.getType());
  }

  @Override
  public void clearBannedAddresses() throws IOException {
    rpcService.post("clear_banned_addresses", Collections.emptyList(), Object.class);
  }

  @Override
  public void pingPeers() throws IOException {
    rpcService.post("ping_peers", Collections.emptyList(), Object.class);
  }

  /** Experiment RPC */
  @Override
  public Cycles dryRunTransaction(Transaction transaction) throws IOException {
    return rpcService.post(
        "dry_run_transaction",
        Collections.singletonList(Convert.parseTransaction(transaction)),
        Cycles.class);
  }

  @Override
  public Cycles estimateCycles(Transaction transaction) throws IOException {
    return rpcService.post(
        "estimate_cycles",
        Collections.singletonList(Convert.parseTransaction(transaction)),
        Cycles.class);
  }

  @Override
  public TipResponse getIndexerTip() throws IOException {
    return this.rpcService.post("get_indexer_tip", Arrays.asList(), TipResponse.class);
  }

  @Override
  public CellsResponse getCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor)
      throws IOException {
    return this.rpcService.post("get_cells",
                                Arrays.asList(searchKey, order, limit, afterCursor),
                                CellsResponse.class);
  }

  @Override
  public TxsWithCell getTransactions(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    searchKey.groupByTransaction = false;
    return this.rpcService.post("get_transactions",
                                Arrays.asList(searchKey, order, limit, afterCursor),
                                TxsWithCell.class);
  }

  @Override
  public TxsWithCells getTransactionsGrouped(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException {
    searchKey.groupByTransaction = true;
    return this.rpcService.post("get_transactions",
                                Arrays.asList(searchKey, order, limit, afterCursor),
                                TxsWithCells.class);
  }

  @Override
  public CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException {
    return this.rpcService.post("get_cells_capacity",
                                Arrays.asList(searchKey),
                                CellCapacityResponse.class);
  }

  @Override
  public long calculateDaoMaximumWithdraw(OutPoint outPoint, byte[] withdrawBlockHash)
      throws IOException {
    return rpcService.post(
        "calculate_dao_maximum_withdraw",
        Arrays.asList(outPoint, withdrawBlockHash),
        Long.class);
  }

  /**
   * Batch RPC request
   *
   * @param requests A list of rpc method and parameters and the first element of each list must be
   *                 rpc method. Example: [["get_block_hash", "0x200"],["get_block_by_number", "0x300"]]
   * @return A list of rpc response
   * @throws IOException Request or response error will throw exception
   */
  @Override
  public List<RpcResponse> batchRPC(List<List> requests) throws IOException {
    return rpcService.batchPost(requests);
  }
}
