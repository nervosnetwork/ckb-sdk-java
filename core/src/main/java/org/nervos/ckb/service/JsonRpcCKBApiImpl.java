package org.nervos.ckb.service;

import java.util.Arrays;
import java.util.Collections;
import org.nervos.ckb.methods.Request;
import org.nervos.ckb.methods.response.*;
import org.nervos.ckb.methods.type.BannedAddress;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.utils.Convert;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class JsonRpcCKBApiImpl implements CKBService {

  protected final APIService apiService;

  public JsonRpcCKBApiImpl(APIService apiService) {
    this.apiService = apiService;
  }

  /** Chain RPC */
  @Override
  public Request<?, CkbBlock> getBlock(String blockHash) {
    return new Request<>(
        "get_block", Collections.singletonList(blockHash), apiService, CkbBlock.class);
  }

  @Override
  public Request<?, CkbBlock> getBlockByNumber(String blockNumber) {
    return new Request<>(
        "get_block_by_number",
        Collections.singletonList(Numeric.toHexString(blockNumber)),
        apiService,
        CkbBlock.class);
  }

  @Override
  public Request<?, CkbTransaction> getTransaction(String transactionHash) {
    return new Request<>(
        "get_transaction",
        Collections.singletonList(transactionHash),
        apiService,
        CkbTransaction.class);
  }

  @Override
  public Request<?, CkbBlockHash> getBlockHash(String blockNumber) {
    return new Request<>(
        "get_block_hash",
        Collections.singletonList(Numeric.toHexString(blockNumber)),
        apiService,
        CkbBlockHash.class);
  }

  @Override
  public Request<?, CkbCellbaseOutputCapacity> getCellbaseOutputCapacityDetails(String blockHash) {
    return new Request<>(
        "get_cellbase_output_capacity_details",
        Collections.singletonList(blockHash),
        apiService,
        CkbCellbaseOutputCapacity.class);
  }

  @Override
  public Request<?, CkbHeader> getTipHeader() {
    return new Request<>(
        "get_tip_header", Collections.<String>emptyList(), apiService, CkbHeader.class);
  }

  @Override
  public Request<?, CkbCells> getCellsByLockHash(
      String lockHash, String fromBlockNumber, String toBlockNumber) {
    return new Request<>(
        "get_cells_by_lock_hash",
        Arrays.asList(
            lockHash, Numeric.toHexString(fromBlockNumber), Numeric.toHexString(toBlockNumber)),
        apiService,
        CkbCells.class);
  }

  @Override
  public Request<?, CkbCell> getLiveCell(OutPoint outPoint, boolean withData) {
    return new Request<>(
        "get_live_cell",
        Arrays.asList(Convert.parseOutPoint(outPoint), withData),
        apiService,
        CkbCell.class);
  }

  @Override
  public Request<?, CkbBlockNumber> getTipBlockNumber() {
    return new Request<>(
        "get_tip_block_number", Collections.<String>emptyList(), apiService, CkbBlockNumber.class);
  }

  @Override
  public Request<?, CkbEpoch> getCurrentEpoch() {
    return new Request<>(
        "get_current_epoch", Collections.<String>emptyList(), apiService, CkbEpoch.class);
  }

  @Override
  public Request<?, CkbEpoch> getEpochByNumber(String epochNumber) {
    return new Request<>(
        "get_epoch_by_number",
        Collections.singletonList(Numeric.toHexString(epochNumber)),
        apiService,
        CkbEpoch.class);
  }

  @Override
  public Request<?, CkbHeader> getHeader(String blockHash) {
    return new Request<>(
        "get_header", Collections.singletonList(blockHash), apiService, CkbHeader.class);
  }

  @Override
  public Request<?, CkbHeader> getHeaderByNumber(String blockNumber) {
    return new Request<>(
        "get_header_by_number",
        Collections.singletonList(Numeric.toHexString(blockNumber)),
        apiService,
        CkbHeader.class);
  }

  /** Stats RPC */
  @Override
  public Request<?, CkbBlockchainInfo> getBlockchainInfo() {
    return new Request<>(
        "get_blockchain_info", Collections.emptyList(), apiService, CkbBlockchainInfo.class);
  }

  @Override
  public Request<?, CkbPeersState> getPeersState() {
    return new Request<>(
        "get_peers_state", Collections.emptyList(), apiService, CkbPeersState.class);
  }

  @Override
  public Request<?, CkbBannedResult> setBan(BannedAddress bannedAddress) {
    return new Request<>(
        "set_ban", Collections.singletonList(bannedAddress), apiService, CkbBannedResult.class);
  }

  @Override
  public Request<?, CkbBannedResultAddresses> getBannedAddress() {
    return new Request<>(
        "get_banned_address", Collections.emptyList(), apiService, CkbBannedResultAddresses.class);
  }

  /** Pool RPC */
  @Override
  public Request<?, CkbTxPoolInfo> txPoolInfo() {
    return new Request<>("tx_pool_info", Collections.emptyList(), apiService, CkbTxPoolInfo.class);
  }

  @Override
  public Request<?, CkbTransactionHash> sendTransaction(Transaction transaction) {
    return new Request<>(
        "send_transaction",
        Collections.singletonList(Convert.parseTransaction(transaction)),
        apiService,
        CkbTransactionHash.class);
  }

  /** Net RPC */
  @Override
  public Request<?, CkbNodeInfo> localNodeInfo() {
    return new Request<>(
        "local_node_info", Collections.<String>emptyList(), apiService, CkbNodeInfo.class);
  }

  @Override
  public Request<?, CkbPeers> getPeers() {
    return new Request<>("get_peers", Collections.<String>emptyList(), apiService, CkbPeers.class);
  }

  /** Experiment RPC */
  @Override
  public Request<?, CkbCycles> dryRunTransaction(Transaction transaction) {
    return new Request<>(
        "dry_run_transaction",
        Collections.singletonList(Convert.parseTransaction(transaction)),
        apiService,
        CkbCycles.class);
  }

  public Request<?, CkbTransactionHash> computeTransactionHash(Transaction transaction) {
    return new Request<>(
        "_compute_transaction_hash",
        Collections.singletonList(Convert.parseTransaction(transaction)),
        apiService,
        CkbTransactionHash.class);
  }

  public Request<?, CkbScriptHash> computeScriptHash(Script script) {
    return new Request<>(
        "_compute_script_hash", Collections.singletonList(script), apiService, CkbScriptHash.class);
  }

  /* Indexer RPC */
  public Request<?, CkbLockHashIndexState> indexLockHash(String lockHash, String blockNumber) {
    return new Request<>(
        "index_lock_hash",
        Arrays.asList(lockHash, Numeric.toHexString(blockNumber)),
        apiService,
        CkbLockHashIndexState.class);
  }

  public Request<?, CkbLockHashIndexState> indexLockHash(String lockHash) {
    return new Request<>(
        "index_lock_hash",
        Collections.singletonList(lockHash),
        apiService,
        CkbLockHashIndexState.class);
  }

  public Request<?, CkbLockHashs> deindexLockHash(String lockHash) {
    return new Request<>(
        "deindex_lock_hash", Collections.singletonList(lockHash), apiService, CkbLockHashs.class);
  }

  public Request<?, CkbLockHashIndexStates> getLockHashIndexStates() {
    return new Request<>(
        "get_lock_hash_index_states",
        Collections.emptyList(),
        apiService,
        CkbLockHashIndexStates.class);
  }

  public Request<?, CkbLiveCells> getLiveCellsByLockHash(
      String lockHash, String page, String pageSize, boolean reverseOrder) {
    return new Request<>(
        "get_live_cells_by_lock_hash",
        Arrays.asList(
            lockHash, Numeric.toHexString(page), Numeric.toHexString(pageSize), reverseOrder),
        apiService,
        CkbLiveCells.class);
  }

  public Request<?, CkbCellTransactions> getTransactionsByLockHash(
      String lockHash, String page, String pageSize, boolean reverseOrder) {
    return new Request<>(
        "get_transactions_by_lock_hash",
        Arrays.asList(
            lockHash, Numeric.toHexString(page), Numeric.toHexString(pageSize), reverseOrder),
        apiService,
        CkbCellTransactions.class);
  }
}
