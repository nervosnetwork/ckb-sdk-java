package org.nervos.ckb.service;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.CkbRpcApi;
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
import org.nervos.ckb.type.SyncState;
import org.nervos.ckb.type.TransactionProof;
import org.nervos.ckb.type.TxPoolInfo;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.param.OutputsValidator;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.type.transaction.TransactionWithStatus;
import org.nervos.ckb.utils.Convert;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
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
  public Block getBlockByNumber(int blockNumber) throws IOException {
    return rpcService.post(
        "get_block_by_number", Collections.singletonList(blockNumber), Block.class);
  }

  @Override
  public TransactionWithStatus getTransaction(byte[] transactionHash) throws IOException {
    return rpcService.post(
        "get_transaction", Collections.singletonList(transactionHash), TransactionWithStatus.class);
  }

  @Override
  public byte[] getBlockHash(int blockNumber) throws IOException {
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
  public BigInteger getTipBlockNumber() throws IOException {
    return rpcService.post(
        "get_tip_block_number", Collections.<String>emptyList(), BigInteger.class);
  }

  @Override
  public Epoch getCurrentEpoch() throws IOException {
    return rpcService.post("get_current_epoch", Collections.<String>emptyList(), Epoch.class);
  }

  @Override
  public Epoch getEpochByNumber(int epochNumber) throws IOException {
    return rpcService.post(
        "get_epoch_by_number", Collections.singletonList(epochNumber), Epoch.class);
  }

  @Override
  public Header getHeader(byte[] blockHash) throws IOException {
    return rpcService.post("get_header", Collections.singletonList(blockHash), Header.class);
  }

  @Override
  public Header getHeaderByNumber(int blockNumber) throws IOException {
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
  public Long getBlockMedianTime(byte[] blockHash) throws IOException {
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
  public void setNetworkActive(Boolean state) throws IOException {
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
            Numeric.toHexStringWithPrefix(BigInteger.valueOf(bannedAddress.banTime)),
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
  public BigInteger calculateDaoMaximumWithdraw(OutPoint outPoint, String withdrawBlockHash)
      throws IOException {
    return rpcService.post(
        "calculate_dao_maximum_withdraw",
        Arrays.asList(outPoint, withdrawBlockHash),
        BigInteger.class);
  }

  /**
   * Batch RPC request
   *
   * @param requests: A list of rpc method and parameters and the first element of each list must be
   *     rpc method. Example: [["get_block_hash", "0x200"],["get_block_by_number", "0x300"]]
   * @return A list of rpc response
   * @throws IOException Request or response error will throw exception
   */
  @Override
  public List<RpcResponse> batchRPC(List<List> requests) throws IOException {
    return rpcService.batchPost(requests);
  }
}
