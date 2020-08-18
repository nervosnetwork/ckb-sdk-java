package service;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.RpcResponse;
import org.nervos.ckb.type.*;
import org.nervos.ckb.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.type.cell.CellTransaction;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.cell.LiveCell;
import org.nervos.ckb.type.param.OutputsValidator;
import org.nervos.ckb.type.transaction.Transaction;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiTest {

  private Api api;

  @BeforeAll
  public void init() {
    api = new Api("http://localhost:8114", false);
  }

  @Test
  public void testGetBlockByNumber() throws IOException {
    Block block = api.getBlockByNumber("0x1");
    Assertions.assertNotNull(block);
  }

  @Test
  public void testGetBlockHashByNumber() throws IOException {
    String blockHash = api.getBlockHash("0x1");
    Assertions.assertNotNull(blockHash);
  }

  @Test
  public void testGetCellbaseOutputCapacityDetails() throws IOException {
    String blockHash = api.getBlockHash("0x1");
    CellbaseOutputCapacity cellbaseOutputCapacity = api.getCellbaseOutputCapacityDetails(blockHash);
    Assertions.assertNotNull(cellbaseOutputCapacity);
  }

  @Test
  public void testGetBlockEconomicState() throws IOException {
    String blockHash = api.getBlockHash("0x2");
    BlockEconomicState blockEconomicState = api.getBlockEconomicState(blockHash);
    Assertions.assertNotNull(blockEconomicState);
  }

  @Test
  public void testBlockAndTransaction() throws IOException {
    String blockHash = api.getBlockHash("0x1");
    Block block = api.getBlock(blockHash);
    Assertions.assertNotNull(block);
    Assertions.assertNotNull(block.header);
  }

  @Test
  public void testTransaction() throws IOException {
    String transactionHash = api.getBlockByNumber("0x1").transactions.get(0).hash;
    Transaction transaction = api.getTransaction(transactionHash).transaction;
    Assertions.assertNotNull(transaction);
  }

  @Test
  public void testGetTipHeader() throws IOException {
    Header header = api.getTipHeader();
    Assertions.assertNotNull(header);
  }

  @Test
  public void testGetTipBlockNumber() throws IOException {
    BigInteger blockNumber = api.getTipBlockNumber();
    Assertions.assertNotNull(blockNumber.toString());
  }

  @Test
  public void testGetCurrentEpoch() throws IOException {
    Epoch epoch = api.getCurrentEpoch();
    Assertions.assertNotNull(epoch);
  }

  @Test
  public void testGetEpochByNumber() throws IOException {
    Epoch epoch = api.getEpochByNumber("0");
    Assertions.assertNotNull(epoch);
  }

  @Test
  public void testGetHeader() throws IOException {
    String blockHash = api.getBlockHash("0x1");
    Header header = api.getHeader(blockHash);
    Assertions.assertNotNull(header);
  }

  @Test
  public void testGetHeaderByNumber() throws IOException {
    Header header = api.getHeaderByNumber("0x1");
    Assertions.assertNotNull(header);
  }

  @Test
  public void testLocalNodeInfo() throws IOException {
    NodeInfo nodeInfo = api.localNodeInfo();
    Assertions.assertNotNull(nodeInfo);
  }

  @Test
  public void testGetPeers() throws IOException {
    List<NodeInfo> peers = api.getPeers();
    Assertions.assertNotNull(peers);
  }

  @Test
  public void testSyncState() throws IOException {
    SyncState state = api.syncState();
    Assertions.assertNotNull(state);
    Assertions.assertNotNull(state.bestKnownBlockNumber);
  }

  @Test
  public void testSetBan() throws IOException {
    BannedAddress bannedAddress =
        new BannedAddress("192.168.0.2", "insert", "1840546800000", true, "test set_ban rpc");
    String banResult = api.setBan(bannedAddress);
    Assertions.assertNull(banResult);
  }

  @Test
  public void testGetBannedAddress() throws IOException {
    List<BannedResultAddress> bannedAddresses = api.getBannedAddress();
    Assertions.assertNotNull(bannedAddresses);
  }

  @Test
  public void testTxPoolInfo() throws IOException {
    TxPoolInfo txPoolInfo = api.txPoolInfo();
    Assertions.assertNotNull(txPoolInfo);
    Assertions.assertNotNull(txPoolInfo.minFeeRate);
    Assertions.assertNotNull(txPoolInfo.tipHash);
  }

  @Test
  public void testClearTxPool() throws IOException {
    String txPoolInfo = api.clearTxPool();
    Assertions.assertNull(txPoolInfo);
  }

  @Test
  public void testGetBlockchainInfo() throws IOException {
    BlockchainInfo blockchainInfo = api.getBlockchainInfo();
    Assertions.assertNotNull(blockchainInfo);
  }

  @Test
  public void testGetPeersState() throws IOException {
    List<PeerState> peerStates = api.getPeersState();
    Assertions.assertNotNull(peerStates);
  }

  @Test
  public void testGetCellsByLockHash() throws IOException {
    List<CellOutputWithOutPoint> cellOutputWithOutPoints =
        api.getCellsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba", "0", "100");
    Assertions.assertTrue(cellOutputWithOutPoints.size() > 0);
  }

  @Test
  public void testGetLiveCell() throws IOException {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            true);
    Assertions.assertNotNull(cellWithStatus);
  }

  @Test
  public void testGetLiveCellWithData() throws IOException {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            true);
    Assertions.assertNotNull(cellWithStatus.cell.data);
  }

  @Test
  public void testGetLiveCellWithoutData() throws IOException {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            false);
    Assertions.assertNull(cellWithStatus.cell.data);
  }

  @Test
  public void testSendTransaction() {
    Assertions.assertThrows(
        IOException.class,
        new Executable() {
          @Override
          public void execute() throws Throwable {
            api.sendTransaction(
                new Transaction(
                    "0",
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()));
          }
        },
        "Transaction Empty");
  }

  @Test
  public void testSendTransactionOutputsValidator() {
    Assertions.assertThrows(
        IOException.class,
        new Executable() {
          @Override
          public void execute() throws Throwable {
            api.sendTransaction(
                new Transaction(
                    "0",
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()),
                OutputsValidator.DEFAULT);
          }
        },
        "Transaction Empty");

    Assertions.assertThrows(
        IOException.class,
        new Executable() {
          @Override
          public void execute() throws Throwable {
            api.sendTransaction(
                new Transaction(
                    "0",
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()),
                OutputsValidator.PASSTHROUGH);
          }
        },
        "Transaction Empty");
  }

  @Test
  public void testDryRunTransaction() throws IOException {
    Cycles cycles =
        api.dryRunTransaction(
            new Transaction(
                "0",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()));
    Assertions.assertNotNull(cycles);
  }

  @Test
  public void testComputeTransactionHash() throws IOException {
    String transactionHash =
        api.computeTransactionHash(
            new Transaction(
                "0",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()));
    Assertions.assertNotNull(transactionHash);
  }

  @Test
  public void testIndexLockHash() throws IOException {
    LockHashIndexState lockHashIndexState =
        api.indexLockHash("0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e");
    Assertions.assertNotNull(lockHashIndexState);
  }

  @Test
  public void testIndexLockHashWithBlockNumber() throws IOException {
    LockHashIndexState lockHashIndexState =
        api.indexLockHash(
            "0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e", "0");
    Assertions.assertNotNull(lockHashIndexState);
  }

  @Test
  public void testDeindexLockHash() throws IOException {
    List<String> lockHashs =
        api.deindexLockHash("0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e");
    Assertions.assertNull(lockHashs);
  }

  @Test
  public void testGetLockHashIndexStates() throws IOException {
    List<LockHashIndexState> lockHashIndexStates = api.getLockHashIndexStates();
    Assertions.assertNotNull(lockHashIndexStates);
  }

  @Test
  public void testGetLiveCellsByLockHash() throws IOException {
    List<LiveCell> liveCells =
        api.getLiveCellsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba",
            "0",
            "100",
            false);
    Assertions.assertNotNull(liveCells);
  }

  @Test
  public void testGetTransactionsByLockHash() throws IOException {
    List<CellTransaction> cellTransactions =
        api.getTransactionsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba",
            "0",
            "100",
            false);
    Assertions.assertNotNull(cellTransactions);
  }

  @Test
  public void testGetCapacityByLockHash() throws Exception {
    // Call index_lock_hash rpc before calling get_capacity_by_lock_hash and wait some time
    // api.indexLockHash("0x1f2615a8dde4e28ca736ff763c2078aff990043f4cbf09eb4b3a58a140a0862d");
    LockHashCapacity lockHashCapacity =
        api.getCapacityByLockHash(
            "0x1f2615a8dde4e28ca736ff763c2078aff990043f4cbf09eb4b3a58a140a0862d");
    Assertions.assertNotNull(lockHashCapacity);
    Assertions.assertNotNull(lockHashCapacity.capacity);
    Assertions.assertNotNull(lockHashCapacity.cellsCount);
  }

  @Test
  public void testBatchRpc() throws IOException {
    List<RpcResponse> rpcResponses =
        api.batchRPC(
            Arrays.asList(
                Arrays.asList("get_block_hash", "0x200"),
                Arrays.asList("get_block_by_number", "300"),
                Arrays.asList("get_header_by_number", 100)));
    Assertions.assertNotNull(rpcResponses);
    Assertions.assertEquals(3, rpcResponses.size());
    Assertions.assertTrue(rpcResponses.get(0).result instanceof String);
    Assertions.assertTrue(
        new Gson().fromJson(rpcResponses.get(1).result.toString(), Block.class).transactions.size()
            > 0);
    Assertions.assertNotNull(
        new Gson().fromJson(rpcResponses.get(2).result.toString(), Header.class).compactTarget);

    Assertions.assertThrows(
        IOException.class,
        new Executable() {
          @Override
          public void execute() throws Throwable {
            api.batchRPC(Collections.singletonList(Arrays.asList(1, "0x300")));
          }
        },
        "RPC method name must be a non-null string");

    Assertions.assertThrows(
        IOException.class,
        new Executable() {
          @Override
          public void execute() throws Throwable {
            api.batchRPC(Collections.singletonList(Collections.EMPTY_LIST));
          }
        },
        "RPC method name must be a non-null string");
  }
}
