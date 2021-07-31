package org.nervos.ckb;

import java.io.IOException;
import java.math.BigInteger;
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
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.SyncState;
import org.nervos.ckb.type.TransactionProof;
import org.nervos.ckb.type.TxPoolInfo;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.param.OutputsValidator;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.type.transaction.TransactionWithStatus;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public interface CkbRpcApi {
  Block getBlock(String blockHash) throws IOException;

  Block getBlockByNumber(String blockNumber) throws IOException;

  TransactionWithStatus getTransaction(String transactionHash) throws IOException;

  String getBlockHash(String blockNumber) throws IOException;

  BlockEconomicState getBlockEconomicState(String blockHash) throws IOException;

  Header getTipHeader() throws IOException;

  CellWithStatus getLiveCell(OutPoint outPoint, boolean withData) throws IOException;

  BigInteger getTipBlockNumber() throws IOException;

  Epoch getCurrentEpoch() throws IOException;

  Epoch getEpochByNumber(String epochNumber) throws IOException;

  Header getHeader(String blockHash) throws IOException;

  Header getHeaderByNumber(String blockNumber) throws IOException;

  TransactionProof getTransactionProof(List<String> txHashes) throws IOException;

  TransactionProof getTransactionProof(List<String> txHashes, String blockHash) throws IOException;

  List<String> verifyTransactionProof(TransactionProof transactionProof) throws IOException;

  Consensus getConsensus() throws IOException;

  String getBlockMedianTime(String blockHash) throws IOException;

  BlockchainInfo getBlockchainInfo() throws IOException;

  TxPoolInfo txPoolInfo() throws IOException;

  String clearTxPool() throws IOException;

  RawTxPool getRawTxPool() throws IOException;

  RawTxPoolVerbose getRawTxPoolVerbose() throws IOException;

  String sendTransaction(Transaction transaction) throws IOException;

  String sendTransaction(Transaction transaction, OutputsValidator outputsValidator)
      throws IOException;

  NodeInfo localNodeInfo() throws IOException;

  List<PeerNodeInfo> getPeers() throws IOException;

  SyncState syncState() throws IOException;

  String setNetworkActive(Boolean state) throws IOException;

  String addNode(String peerId, String address) throws IOException;

  String removeNode(String peerId) throws IOException;

  String setBan(BannedAddress bannedAddress) throws IOException;

  List<BannedResultAddress> getBannedAddresses() throws IOException;

  String clearBannedAddresses() throws IOException;

  String pingPeers() throws IOException;

  Cycles dryRunTransaction(Transaction transaction) throws IOException;

  @Deprecated
  String computeTransactionHash(Transaction transaction) throws IOException;

  @Deprecated
  String computeScriptHash(Script script) throws IOException;

  String calculateDaoMaximumWithdraw(OutPoint outPoint, String withdrawBlockHash)
      throws IOException;

  List<RpcResponse> batchRPC(List<List> requests) throws IOException;
}
