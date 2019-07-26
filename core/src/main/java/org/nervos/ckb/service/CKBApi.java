package org.nervos.ckb.service;

import org.nervos.ckb.methods.Request;
import org.nervos.ckb.methods.response.*;
import org.nervos.ckb.methods.type.BannedAddress;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.transaction.Transaction;

/** Created by duanyytop on 2018-12-20. Copyright © 2018 Nervos Foundation. All rights reserved. */
public interface CKBApi {

  /** Chain RPC */
  Request<?, CkbBlock> getBlock(String blockHash);

  Request<?, CkbBlock> getBlockByNumber(String blockNumber);

  Request<?, CkbTransaction> getTransaction(String transactionHash);

  Request<?, CkbBlockHash> getBlockHash(String blockNumber);

  Request<?, CkbCellbaseOutputCapacity> getCellbaseOutputCapacityDetails(String blockHash);

  Request<?, CkbHeader> getTipHeader();

  Request<?, CkbCells> getCellsByLockHash(
      String lockHash, String fromBlockNumber, String toBlockNumber);

  Request<?, CkbCell> getLiveCell(OutPoint outPoint);

  Request<?, CkbBlockNumber> getTipBlockNumber();

  Request<?, CkbEpoch> getCurrentEpoch();

  Request<?, CkbEpoch> getEpochByNumber(String epochNumber);

  Request<?, CkbHeader> getHeader(String blockHash);

  Request<?, CkbHeader> getHeaderByNumber(String blockNumber);

  /** Stats RPC */
  Request<?, CkbBlockchainInfo> getBlockchainInfo();

  Request<?, CkbPeersState> getPeersState();

  /** Pool RPC */
  Request<?, CkbTransactionHash> sendTransaction(Transaction transaction);

  Request<?, CkbTxPoolInfo> txPoolInfo();

  /** Net RPC */
  Request<?, CkbNodeInfo> localNodeInfo();

  Request<?, CkbBannedResult> setBan(BannedAddress bannedAddress);

  Request<?, CkbBannedResultAddresses> getBannedAddress();

  Request<?, CkbPeers> getPeers();

  /** Experiment RPC */
  Request<?, CkbCycles> dryRunTransaction(Transaction transaction);

  Request<?, CkbTransactionHash> computeTransactionHash(Transaction transaction);

  /* Indexer RPC */
  Request<?, CkbLockHashIndexState> indexLockHash(String lockHash, String blockNumber);

  Request<?, CkbLockHashIndexState> indexLockHash(String lockHash);

  Request<?, CkbLockHashs> deindexLockHash(String lockHash);

  Request<?, CkbLockHashIndexStates> getLockHashIndexStates();

  Request<?, CkbLiveCells> getLiveCellsByLockHash(
      String lockHash, String page, String pageSize, boolean reverseOrder);

  Request<?, CkbCellTransactions> getTransactionsByLockHash(
      String lockHash, String page, String pageSize, boolean reverseOrder);
}
