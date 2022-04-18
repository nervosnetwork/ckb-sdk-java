package org.nervos.ckb;

import java.io.IOException;
import java.util.List;
import org.nervos.ckb.service.RpcResponse;
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

public interface CkbRpcApi {
  Block getBlock(byte[] blockHash) throws IOException;

  Block getBlockByNumber(long blockNumber) throws IOException;

  TransactionWithStatus getTransaction(byte[] transactionHash) throws IOException;

  byte[] getBlockHash(long blockNumber) throws IOException;

  BlockEconomicState getBlockEconomicState(byte[] blockHash) throws IOException;

  Header getTipHeader() throws IOException;

  CellWithStatus getLiveCell(OutPoint outPoint, boolean withData) throws IOException;

  long getTipBlockNumber() throws IOException;

  Epoch getCurrentEpoch() throws IOException;

  Epoch getEpochByNumber(long epochNumber) throws IOException;

  Header getHeader(byte[] blockHash) throws IOException;

  Header getHeaderByNumber(long blockNumber) throws IOException;

  TransactionProof getTransactionProof(List<byte[]> txHashes) throws IOException;

  TransactionProof getTransactionProof(List<byte[]> txHashes, byte[] blockHash) throws IOException;

  List<byte[]> verifyTransactionProof(TransactionProof transactionProof) throws IOException;

  Block getForkBlock(byte[] blockHash) throws IOException;

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

  Cycles dryRunTransaction(Transaction transaction) throws IOException;

  long calculateDaoMaximumWithdraw(OutPoint outPoint, byte[] withdrawBlockHash)
      throws IOException;

  List<RpcResponse> batchRPC(List<List> requests) throws IOException;
}
