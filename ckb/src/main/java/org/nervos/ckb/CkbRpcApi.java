package org.nervos.ckb;

import org.nervos.ckb.service.RpcResponse;
import org.nervos.ckb.type.*;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKey;
import org.nervos.indexer.model.resp.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public interface CkbRpcApi {
  Block getBlock(byte[] blockHash) throws IOException;

  BlockWithCycles getBlock(byte[] blockHash, boolean with_cycles) throws IOException;

  PackedBlockWithCycles getPackedBlock(byte[] blockHash, boolean with_cycles) throws IOException;

  Block getBlockByNumber(long blockNumber) throws IOException;

  BlockWithCycles getBlockByNumber(long blockNumber, boolean with_cycles) throws IOException;

  PackedBlockWithCycles getPackedBlockByNumber(long blockNumber, boolean with_cycles) throws IOException;

  default TransactionWithStatus getTransaction(@Nonnull byte[] transactionHash) throws IOException {
    return getTransaction(transactionHash, null);
  }

  TransactionWithStatus getTransaction(@Nonnull byte[] transactionHash, @Nullable Boolean onlyCommitted) throws IOException;

  /**
   * get transaction with verbosity value is 1
   *
   * @param transactionHash the transaction hash
   * @return the RPC does not return the transaction content and the field transaction must be null.
   * @throws IOException Error occurs while communicating with the RPC server or if the transaction hash is invalid
   */
  default TransactionWithStatus getTransactionStatus(@Nonnull byte[] transactionHash) throws IOException {
    return getTransactionStatus(transactionHash, null);
  }

  TransactionWithStatus getTransactionStatus(@Nonnull byte[] transactionHash, @Nullable Boolean onlyCommitted) throws IOException;

  default PackedTransactionWithStatus getPackedTransaction(@Nonnull byte[] transactionHash) throws IOException {
    return getPackedTransaction(transactionHash, null);
  }

  PackedTransactionWithStatus getPackedTransaction(@Nonnull byte[] transactionHash, @Nullable Boolean onlyCommitted) throws IOException;

  byte[] getBlockHash(long blockNumber) throws IOException;

  BlockEconomicState getBlockEconomicState(byte[] blockHash) throws IOException;

  Header getTipHeader() throws IOException;

  PackedHeader getPackedTipHeader() throws IOException;

  CellWithStatus getLiveCell(OutPoint outPoint, boolean withData) throws IOException;

  long getTipBlockNumber() throws IOException;

  Epoch getCurrentEpoch() throws IOException;

  Epoch getEpochByNumber(long epochNumber) throws IOException;

  Header getHeader(byte[] blockHash) throws IOException;

  PackedHeader getPackedHeader(byte[] blockHash) throws IOException;

  Header getHeaderByNumber(long blockNumber) throws IOException;

  PackedHeader getPackedHeaderByNumber(long blockNumber) throws IOException;

  TransactionProof getTransactionProof(List<byte[]> txHashes) throws IOException;

  TransactionProof getTransactionProof(List<byte[]> txHashes, byte[] blockHash) throws IOException;

  List<byte[]> verifyTransactionProof(TransactionProof transactionProof) throws IOException;

  TransactionAndWitnessProof getTransactionAndWitnessProof(List<byte[]> txHashes, byte[] blockHash) throws IOException;

  List<byte[]> verifyTransactionAndWitnessProof(TransactionAndWitnessProof proof) throws IOException;

  Block getForkBlock(byte[] blockHash) throws IOException;

  PackedBlockWithCycles getPackedForkBlock(byte[] blockHash) throws IOException;

  Consensus getConsensus() throws IOException;

  long getBlockMedianTime(byte[] blockHash) throws IOException;

  BlockchainInfo getBlockchainInfo() throws IOException;

  TxPoolInfo txPoolInfo() throws IOException;

  void clearTxPool() throws IOException;

  RawTxPool getRawTxPool() throws IOException;

  RawTxPoolVerbose getRawTxPoolVerbose() throws IOException;

  byte[] sendTransaction(Transaction transaction) throws IOException;

  byte[] sendTransaction(Transaction transaction, OutputsValidator outputsValidator)
      throws IOException;

  byte[] sendTestTransaction(Transaction transaction) throws IOException;

  byte[] sendTestTransaction(Transaction transaction, OutputsValidator outputsValidator)
      throws IOException;
	
  EntryCompleted testTxPoolAccept(Transaction transaction) throws IOException;

  EntryCompleted testTxPoolAccept(Transaction transaction, OutputsValidator outputsValidator)
      throws IOException;

  NodeInfo localNodeInfo() throws IOException;

  List<PeerNodeInfo> getPeers() throws IOException;

  SyncState syncState() throws IOException;

  void setNetworkActive(boolean state) throws IOException;

  void addNode(String peerId, String address) throws IOException;

  void removeNode(String peerId) throws IOException;

  void setBan(BannedAddress bannedAddress) throws IOException;

  List<BannedResultAddress> getBannedAddresses() throws IOException;

  void clearBannedAddresses() throws IOException;

  void pingPeers() throws IOException;

  @Deprecated
  Cycles dryRunTransaction(Transaction transaction) throws IOException;

  Cycles estimateCycles(Transaction transaction) throws IOException;

  TipResponse getIndexerTip() throws IOException;

  CellsResponse getCells(SearchKey searchKey, Order order, int limit, byte[] afterCursor)
      throws IOException;

  TxsWithCell getTransactions(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException;

  TxsWithCells getTransactionsGrouped(
      SearchKey searchKey, Order order, int limit, byte[] afterCursor) throws IOException;

  CellCapacityResponse getCellsCapacity(SearchKey searchKey) throws IOException;

  long calculateDaoMaximumWithdraw(OutPoint outPoint, byte[] withdrawBlockHash)
      throws IOException;

  List<RpcResponse> batchRPC(List<List> requests) throws IOException;

  /**
   * Get the fee_rate statistics of confirmed blocks on the chain
   *
   * @param target Specify the number (1 - 101) of confirmed blocks to be counted.
   *               If the number is even, automatically add one. If not specified(null), defaults to 21.
   * @return Returns the fee_rate statistics of confirmed blocks on the chain.
   * @throws IOException if error there is an error
   */
  FeeRateStatistics getFeeRateStatistics(Integer target) throws IOException;
}
