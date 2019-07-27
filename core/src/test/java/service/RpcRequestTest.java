package service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.methods.type.cell.*;
import org.nervos.ckb.methods.type.transaction.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
@Ignore
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RpcRequestTest {

  private CKBService ckbService;

  @BeforeAll
  public void initService() {
    HttpService.setDebug(false);
    ckbService = CKBService.build(new HttpService("http://localhost:8114"));
  }

  @Test
  public void testGetBlockByNumber() throws IOException {
    Block block = ckbService.getBlockByNumber("1").send().getBlock();
    Assertions.assertNotNull(block);
  }

  @Test
  public void testGetBlockHashByNumber() throws IOException {
    String blockHash = ckbService.getBlockHash("1").send().getBlockHash();
    Assertions.assertNotNull(blockHash);
  }

  @Test
  public void testGetCellbaseOutputCapacityDetails() throws IOException {
    String blockHash = ckbService.getBlockHash("1").send().getBlockHash();
    CellbaseOutputCapacity cellbaseOutputCapacity =
        ckbService.getCellbaseOutputCapacityDetails(blockHash).send().getCellbaseOutputCapacity();
    Assertions.assertNotNull(cellbaseOutputCapacity);
  }

  @Test
  public void testBlockAndTransaction() throws IOException {
    String blockHash = ckbService.getBlockHash("1").send().getBlockHash();
    Block block = ckbService.getBlock(blockHash).send().getBlock();
    Assertions.assertNotNull(block);
    Assertions.assertNotNull(block.header);
  }

  @Test
  public void testTransaction() throws IOException {
    String transactionHash =
        ckbService.getBlockByNumber("1").send().getBlock().transactions.get(0).hash;
    Transaction transaction =
        ckbService.getTransaction(transactionHash).send().getTransaction().transaction;
    Assertions.assertNotNull(transaction);
  }

  @Test
  public void testGetTipHeader() throws IOException {
    Header header = ckbService.getTipHeader().send().getHeader();
    Assertions.assertNotNull(header);
  }

  @Test
  public void testGetTipBlockNumber() throws IOException {
    BigInteger blockNumber = ckbService.getTipBlockNumber().send().getBlockNumber();
    Assertions.assertNotNull(blockNumber.toString());
  }

  @Test
  public void testGetCurrentEpoch() throws IOException {
    Epoch epoch = ckbService.getCurrentEpoch().send().getEpoch();
    Assertions.assertNotNull(epoch);
  }

  @Test
  public void testGetEpochByNumber() throws IOException {
    Epoch epoch = ckbService.getEpochByNumber("0").send().getEpoch();
    Assertions.assertNotNull(epoch);
  }

  @Test
  public void testGetHeader() throws IOException {
    String blockHash = ckbService.getBlockHash("1").send().getBlockHash();
    Header header = ckbService.getHeader(blockHash).send().getHeader();
    Assertions.assertNotNull(header);
  }

  @Test
  public void testGetHeaderByNumber() throws IOException {
    Header header = ckbService.getHeaderByNumber("1").send().getHeader();
    Assertions.assertNotNull(header);
  }

  @Test
  public void localNodeInfo() throws IOException {
    NodeInfo nodeInfo = ckbService.localNodeInfo().send().getNodeInfo();
    Assertions.assertNotNull(nodeInfo);
  }

  @Test
  public void getPeers() throws IOException {
    List<NodeInfo> peers = ckbService.getPeers().send().getPeers();
    Assertions.assertNotNull(peers);
  }

  @Test
  public void testSetBan() throws IOException {
    BannedAddress bannedAddress =
        new BannedAddress("192.168.0.2", "insert", "1840546800000", true, "test set_ban rpc");
    String banResult = ckbService.setBan(bannedAddress).send().getBanResult();
    Assertions.assertNull(banResult);
  }

  @Test
  public void testGetBannedAddress() throws IOException {
    List<BannedResultAddress> bannedAddresses =
        ckbService.getBannedAddress().send().getBannedResultAddresses();
    Assertions.assertNotNull(bannedAddresses);
  }

  @Test
  public void txPoolInfo() throws IOException {
    TxPoolInfo txPoolInfo = ckbService.txPoolInfo().send().getTxPoolInfo();
    Assertions.assertNotNull(txPoolInfo);
  }

  @Test
  public void testGetBlockchainInfo() throws IOException {
    BlockchainInfo blockchainInfo = ckbService.getBlockchainInfo().send().getBlockchainInfo();
    Assertions.assertNotNull(blockchainInfo);
  }

  @Test
  public void testGetPeersState() throws IOException {
    List<PeerState> peerStates = ckbService.getPeersState().send().getPeersState();
    Assertions.assertNotNull(peerStates);
  }

  @Test
  public void testGetCellsByLockHash() throws IOException {
    List<CellOutputWithOutPoint> cellOutputWithOutPoints =
        ckbService
            .getCellsByLockHash(
                "0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e", "0", "100")
            .send()
            .getCells();
    Assertions.assertTrue(cellOutputWithOutPoints.size() > 0);
  }

  @Test
  public void testGetLiveCell() throws IOException {
    Cell cell =
        ckbService
            .getLiveCell(
                new OutPoint(
                    new CellOutPoint(
                        "0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff", "0")))
            .send()
            .getCell();
    Assertions.assertNotNull(cell);
  }

  @Test
  public void testSendTransaction() throws IOException {
    String transactionHash =
        ckbService
            .sendTransaction(
                new Transaction(
                    "0",
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()))
            .send()
            .getTransactionHash();
    Assertions.assertNotNull(transactionHash);
  }

  @Test
  public void testDryRunTransaction() throws IOException {
    Cycles cycles =
        ckbService
            .dryRunTransaction(
                new Transaction(
                    "0",
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()))
            .send()
            .getCycles();
    Assertions.assertNotNull(cycles);
  }

  @Test
  public void testComputeTransactionHash() throws IOException {
    String transactionHash =
        ckbService
            .computeTransactionHash(
                new Transaction(
                    "0",
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()))
            .send()
            .getTransactionHash();
    Assertions.assertNotNull(transactionHash);
  }

  @Test
  public void testIndexLockHash() throws IOException {
    LockHashIndexState lockHashIndexState =
        ckbService
            .indexLockHash("0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e")
            .send()
            .getLockHashIndexState();
    Assertions.assertNotNull(lockHashIndexState);
  }

  @Test
  public void testIndexLockHashWithBlockNumber() throws IOException {
    LockHashIndexState lockHashIndexState =
        ckbService
            .indexLockHash(
                "0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e", "0")
            .send()
            .getLockHashIndexState();
    Assertions.assertNotNull(lockHashIndexState);
  }

  @Test
  public void testDeindexLockHash() throws IOException {
    List<String> lockHashs =
        ckbService
            .deindexLockHash("0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e")
            .send()
            .getLockHashs();
    Assertions.assertNull(lockHashs);
  }

  @Test
  public void testGetLockHashIndexStates() throws IOException {
    List<LockHashIndexState> lockHashIndexStates =
        ckbService.getLockHashIndexStates().send().getLockHashIndexStates();
    Assertions.assertNotNull(lockHashIndexStates);
  }

  @Test
  public void testGetLiveCellsByLockHash() throws IOException {
    List<LiveCell> liveCells =
        ckbService
            .getLiveCellsByLockHash(
                "0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e",
                "0",
                "100",
                false)
            .send()
            .getLiveCells();
    Assertions.assertNotNull(liveCells);
  }

  @Test
  public void testGetTransactionsByLockHash() throws IOException {
    List<CellTransaction> cellTransactions =
        ckbService
            .getTransactionsByLockHash(
                "0x59d90b1718471f5802de59501604100a5e3b463865cdfe56fa70ed23865ee32e",
                "0",
                "100",
                false)
            .send()
            .getCellTransactions();
    Assertions.assertNotNull(cellTransactions);
  }
}
