package service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.*;
import org.nervos.ckb.type.cell.CellOutputWithOutPoint;
import org.nervos.ckb.type.cell.CellTransaction;
import org.nervos.ckb.type.cell.CellWithStatus;
import org.nervos.ckb.type.cell.LiveCell;
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
  public void testGetBlockByNumber() {
    Block block = api.getBlockByNumber("0x1");
    Assertions.assertNotNull(block);
  }

  @Test
  public void testGetBlockHashByNumber() {
    String blockHash = api.getBlockHash("0x1");
    Assertions.assertNotNull(blockHash);
  }

  @Test
  public void testGetCellbaseOutputCapacityDetails() {
    String blockHash = api.getBlockHash("0x1");
    CellbaseOutputCapacity cellbaseOutputCapacity = api.getCellbaseOutputCapacityDetails(blockHash);
    Assertions.assertNotNull(cellbaseOutputCapacity);
  }

  @Test
  public void testBlockAndTransaction() {
    String blockHash = api.getBlockHash("0x1");
    Block block = api.getBlock(blockHash);
    Assertions.assertNotNull(block);
    Assertions.assertNotNull(block.header);
  }

  @Test
  public void testTransaction() {
    String transactionHash = api.getBlockByNumber("0x1").transactions.get(0).hash;
    Transaction transaction = api.getTransaction(transactionHash).transaction;
    Assertions.assertNotNull(transaction);
  }

  @Test
  public void testGetTipHeader() {
    Header header = api.getTipHeader();
    Assertions.assertNotNull(header);
  }

  @Test
  public void testGetTipBlockNumber() {
    BigInteger blockNumber = api.getTipBlockNumber();
    Assertions.assertNotNull(blockNumber.toString());
  }

  @Test
  public void testGetCurrentEpoch() {
    Epoch epoch = api.getCurrentEpoch();
    Assertions.assertNotNull(epoch);
  }

  @Test
  public void testGetEpochByNumber() {
    Epoch epoch = api.getEpochByNumber("0");
    Assertions.assertNotNull(epoch);
  }

  @Test
  public void testGetHeader() {
    String blockHash = api.getBlockHash("0x1");
    Header header = api.getHeader(blockHash);
    Assertions.assertNotNull(header);
  }

  @Test
  public void testGetHeaderByNumber() {
    Header header = api.getHeaderByNumber("0x1");
    Assertions.assertNotNull(header);
  }

  @Test
  public void localNodeInfo() {
    NodeInfo nodeInfo = api.localNodeInfo();
    Assertions.assertNotNull(nodeInfo);
  }

  @Test
  public void getPeers() {
    List<NodeInfo> peers = api.getPeers();
    Assertions.assertNotNull(peers);
  }

  @Test
  public void testSetBan() {
    BannedAddress bannedAddress =
        new BannedAddress("192.168.0.2", "insert", "1840546800000", true, "test set_ban rpc");
    String banResult = api.setBan(bannedAddress);
    Assertions.assertNull(banResult);
  }

  @Test
  public void testGetBannedAddress() {
    List<BannedResultAddress> bannedAddresses = api.getBannedAddress();
    Assertions.assertNotNull(bannedAddresses);
  }

  @Test
  public void txPoolInfo() {
    TxPoolInfo txPoolInfo = api.txPoolInfo();
    Assertions.assertNotNull(txPoolInfo);
  }

  @Test
  public void testGetBlockchainInfo() {
    BlockchainInfo blockchainInfo = api.getBlockchainInfo();
    Assertions.assertNotNull(blockchainInfo);
  }

  @Test
  public void testGetPeersState() {
    List<PeerState> peerStates = api.getPeersState();
    Assertions.assertNotNull(peerStates);
  }

  @Test
  public void testGetCellsByLockHash() {
    List<CellOutputWithOutPoint> cellOutputWithOutPoints =
        api.getCellsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba", "0", "100");
    Assertions.assertTrue(cellOutputWithOutPoints.size() > 0);
  }

  @Test
  public void testGetLiveCell() {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            true);
    Assertions.assertNotNull(cellWithStatus);
  }

  @Test
  public void testGetLiveCellWithData() {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            true);
    Assertions.assertNotNull(cellWithStatus.cell.data);
  }

  @Test
  public void testGetLiveCellWithoutData() {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint("0xde7ac423660b95df1fd8879a54a98020bcbb30fc9bfcf13da757e99b30effd8d", "0"),
            false);
    Assertions.assertNull(cellWithStatus.cell.data);
  }

  @Test
  public void testSendTransaction() {
    String transactionHash =
        api.sendTransaction(
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
  public void testDryRunTransaction() {
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
  public void testComputeTransactionHash() {
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
  public void testIndexLockHash() {
    LockHashIndexState lockHashIndexState =
        api.indexLockHash("0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e");
    Assertions.assertNotNull(lockHashIndexState);
  }

  @Test
  public void testIndexLockHashWithBlockNumber() {
    LockHashIndexState lockHashIndexState =
        api.indexLockHash(
            "0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e", "0");
    Assertions.assertNotNull(lockHashIndexState);
  }

  @Test
  public void testDeindexLockHash() {
    List<String> lockHashs =
        api.deindexLockHash("0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e");
    Assertions.assertNull(lockHashs);
  }

  @Test
  public void testGetLockHashIndexStates() {
    List<LockHashIndexState> lockHashIndexStates = api.getLockHashIndexStates();
    Assertions.assertNotNull(lockHashIndexStates);
  }

  @Test
  public void testGetLiveCellsByLockHash() {
    List<LiveCell> liveCells =
        api.getLiveCellsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba",
            "0",
            "100",
            false);
    Assertions.assertNotNull(liveCells);
  }

  @Test
  public void testGetTransactionsByLockHash() {
    List<CellTransaction> cellTransactions =
        api.getTransactionsByLockHash(
            "0xecaeea8c8581d08a3b52980272001dbf203bc6fa2afcabe7cc90cc2afff488ba",
            "0",
            "100",
            false);
    Assertions.assertNotNull(cellTransactions);
  }
}
