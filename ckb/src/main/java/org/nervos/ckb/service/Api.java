package org.nervos.ckb.service;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.type.*;
import org.nervos.ckb.type.cell.CellTransaction;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.cell.LiveCell;
import org.nervos.ckb.type.param.OutputsValidator;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.type.transaction.TransactionWithStatus;
import org.nervos.ckb.utils.Convert;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Api {

  private RpcService rpcService;

  public Api(String nodeUrl) {
    this(nodeUrl, false);
  }

  public Api(String nodeUrl, boolean isDebug) {
    rpcService = new RpcService(nodeUrl, isDebug);
  }

  public Block getBlock(String blockHash) throws IOException {
    return rpcService.post("get_block", Collections.singletonList(blockHash), Block.class);
  }

  public Block getBlockByNumber(String blockNumber) throws IOException {
    return rpcService.post(
        "get_block_by_number",
        Collections.singletonList(Numeric.toHexString(blockNumber)),
        Block.class);
  }

  public TransactionWithStatus getTransaction(String transactionHash) throws IOException {
    return rpcService.post(
        "get_transaction", Collections.singletonList(transactionHash), TransactionWithStatus.class);
  }

  public String getBlockHash(String blockNumber) throws IOException {
    return rpcService.post(
        "get_block_hash",
        Collections.singletonList(Numeric.toHexString(blockNumber)),
        String.class);
  }

  public CellbaseOutputCapacity getCellbaseOutputCapacityDetails(String blockHash)
      throws IOException {
    return rpcService.post(
        "get_cellbase_output_capacity_details",
        Collections.singletonList(blockHash),
        CellbaseOutputCapacity.class);
  }

  public BlockEconomicState getBlockEconomicState(String blockHash) throws IOException {
    return rpcService.post(
        "get_block_economic_state", Collections.singletonList(blockHash), BlockEconomicState.class);
  }

  public Header getTipHeader() throws IOException {
    return rpcService.post("get_tip_header", Collections.<String>emptyList(), Header.class);
  }

  public CellWithStatus getLiveCell(OutPoint outPoint, boolean withData) throws IOException {
    return rpcService.post(
        "get_live_cell",
        Arrays.asList(Convert.parseOutPoint(outPoint), withData),
        CellWithStatus.class);
  }

  public BigInteger getTipBlockNumber() throws IOException {
    String blockNumber =
        rpcService.post("get_tip_block_number", Collections.<String>emptyList(), String.class);
    return Numeric.toBigInt(blockNumber);
  }

  public Epoch getCurrentEpoch() throws IOException {
    return rpcService.post("get_current_epoch", Collections.<String>emptyList(), Epoch.class);
  }

  public Epoch getEpochByNumber(String epochNumber) throws IOException {
    return rpcService.post(
        "get_epoch_by_number",
        Collections.singletonList(Numeric.toHexString(epochNumber)),
        Epoch.class);
  }

  public Header getHeader(String blockHash) throws IOException {
    return rpcService.post("get_header", Collections.singletonList(blockHash), Header.class);
  }

  public Header getHeaderByNumber(String blockNumber) throws IOException {
    return rpcService.post(
        "get_header_by_number",
        Collections.singletonList(Numeric.toHexString(blockNumber)),
        Header.class);
  }

  public TransactionProof getTransactionProof(List<String> txHashes) throws IOException {
    return rpcService.post(
        "get_transaction_proof", Collections.singletonList(txHashes), TransactionProof.class);
  }

  public TransactionProof getTransactionProof(List<String> txHashes, String blockHash)
      throws IOException {
    return rpcService.post(
        "get_transaction_proof", Arrays.asList(txHashes, blockHash), TransactionProof.class);
  }

  public String verifyTransactionProof(TransactionProof transactionProof) throws IOException {
    return rpcService.post(
        "verify_transaction_proof", Collections.singletonList(transactionProof), String.class);
  }

  public Consensus getConsensus() throws IOException {
    return rpcService.post("get_consensus", Collections.emptyList(), Consensus.class);
  }

  /** Stats RPC */
  public BlockchainInfo getBlockchainInfo() throws IOException {
    return rpcService.post("get_blockchain_info", Collections.emptyList(), BlockchainInfo.class);
  }

  @Deprecated
  public List<PeerState> getPeersState() throws IOException {
    return rpcService.post(
        "get_peers_state", Collections.emptyList(), new TypeToken<List<PeerState>>() {}.getType());
  }

  /** Pool RPC */
  public TxPoolInfo txPoolInfo() throws IOException {
    return rpcService.post("tx_pool_info", Collections.emptyList(), TxPoolInfo.class);
  }

  public String clearTxPool() throws IOException {
    return rpcService.post("clear_tx_pool", Collections.emptyList(), String.class);
  }

  public RawTxPool getRawTxPool() throws IOException {
    return rpcService.post("get_raw_tx_pool", Collections.emptyList(), RawTxPool.class);
  }

  public RawTxPoolVerbose getRawTxPoolVerbose() throws IOException {
    return rpcService.post(
        "get_raw_tx_pool", Collections.singletonList(true), RawTxPoolVerbose.class);
  }

  public String sendTransaction(Transaction transaction) throws IOException {
    return rpcService.post(
        "send_transaction",
        Collections.singletonList(Convert.parseTransaction(transaction)),
        String.class);
  }

  public String sendTransaction(Transaction transaction, OutputsValidator outputsValidator)
      throws IOException {
    return rpcService.post(
        "send_transaction",
        Arrays.asList(Convert.parseTransaction(transaction), outputsValidator.getValue()),
        String.class);
  }

  /** Net RPC */
  public NodeInfo localNodeInfo() throws IOException {
    return rpcService.post("local_node_info", Collections.emptyList(), NodeInfo.class);
  }

  public List<PeerNodeInfo> getPeers() throws IOException {
    return rpcService.post(
        "get_peers", Collections.emptyList(), new TypeToken<List<PeerNodeInfo>>() {}.getType());
  }

  public SyncState syncState() throws IOException {
    return rpcService.post("sync_state", Collections.emptyList(), SyncState.class);
  }

  public String setNetworkActive(Boolean state) throws IOException {
    return rpcService.post("set_network_active", Collections.singletonList(state), String.class);
  }

  public String addNode(String peerId, String address) throws IOException {
    return rpcService.post("add_node", Arrays.asList(peerId, address), String.class);
  }

  public String removeNode(String peerId) throws IOException {
    return rpcService.post("remove_node", Collections.singletonList(peerId), String.class);
  }

  public String setBan(BannedAddress bannedAddress) throws IOException {
    return rpcService.post(
        "set_ban",
        Arrays.asList(
            bannedAddress.address,
            bannedAddress.command,
            Numeric.toHexStringWithPrefix(new BigInteger(bannedAddress.banTime)),
            bannedAddress.absolute,
            bannedAddress.reason),
        String.class);
  }

  public List<BannedResultAddress> getBannedAddresses() throws IOException {
    return rpcService.post(
        "get_banned_addresses",
        Collections.emptyList(),
        new TypeToken<List<BannedResultAddress>>() {}.getType());
  }

  public String clearBannedAddresses() throws IOException {
    return rpcService.post("clear_banned_addresses", Collections.emptyList(), String.class);
  }

  public String pingPeers() throws IOException {
    return rpcService.post("ping_peers", Collections.emptyList(), String.class);
  }

  /** Experiment RPC */
  public Cycles dryRunTransaction(Transaction transaction) throws IOException {
    return rpcService.post(
        "dry_run_transaction",
        Collections.singletonList(Convert.parseTransaction(transaction)),
        Cycles.class);
  }

  @Deprecated
  public String computeTransactionHash(Transaction transaction) throws IOException {
    return rpcService.post(
        "_compute_transaction_hash",
        Collections.singletonList(Convert.parseTransaction(transaction)),
        String.class);
  }

  @Deprecated
  public String computeScriptHash(Script script) throws IOException {
    return rpcService.post("_compute_script_hash", Collections.singletonList(script), String.class);
  }

  public String calculateDaoMaximumWithdraw(OutPoint outPoint, String withdrawBlockHash)
      throws IOException {
    return rpcService.post(
        "calculate_dao_maximum_withdraw", Arrays.asList(outPoint, withdrawBlockHash), String.class);
  }

  /* Indexer RPC */

  @Deprecated
  public LockHashIndexState indexLockHash(String lockHash) throws IOException {
    return rpcService.post(
        "index_lock_hash", Collections.singletonList(lockHash), LockHashIndexState.class);
  }

  @Deprecated
  public LockHashIndexState indexLockHash(String lockHash, String indexFrom) throws IOException {
    return rpcService.post(
        "index_lock_hash",
        Arrays.asList(lockHash, Numeric.toHexString(indexFrom)),
        LockHashIndexState.class);
  }

  @Deprecated
  public List<String> deindexLockHash(String lockHash) throws IOException {
    return rpcService.post(
        "deindex_lock_hash",
        Collections.singletonList(lockHash),
        new TypeToken<List<String>>() {}.getType());
  }

  @Deprecated
  public List<LockHashIndexState> getLockHashIndexStates() throws IOException {
    return rpcService.post(
        "get_lock_hash_index_states",
        Collections.emptyList(),
        new TypeToken<List<LockHashIndexState>>() {}.getRawType());
  }

  @Deprecated
  public List<LiveCell> getLiveCellsByLockHash(
      String lockHash, String page, String pageSize, boolean reverseOrder) throws IOException {
    return rpcService.post(
        "get_live_cells_by_lock_hash",
        Arrays.asList(
            lockHash, Numeric.toHexString(page), Numeric.toHexString(pageSize), reverseOrder),
        new TypeToken<List<LiveCell>>() {}.getType());
  }

  @Deprecated
  public List<CellTransaction> getTransactionsByLockHash(
      String lockHash, String page, String pageSize, boolean reverseOrder) throws IOException {
    return rpcService.post(
        "get_transactions_by_lock_hash",
        Arrays.asList(
            lockHash, Numeric.toHexString(page), Numeric.toHexString(pageSize), reverseOrder),
        new TypeToken<List<CellTransaction>>() {}.getType());
  }

  @Deprecated
  public LockHashCapacity getCapacityByLockHash(String lockHash) throws IOException {
    return rpcService.post(
        "get_capacity_by_lock_hash", Collections.singletonList(lockHash), LockHashCapacity.class);
  }

  /**
   * Batch RPC request
   *
   * @param requests: A list of rpc method and parameters and the first element of each list must be
   *     rpc method. Example: [["get_block_hash", "0x200"],["get_block_by_number", "0x300"]]
   * @return A list of rpc response
   * @throws IOException Request or response error will throw exception
   */
  public List<RpcResponse> batchRPC(List<List> requests) throws IOException {
    return rpcService.batchPost(requests);
  }
}
