package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.service.RpcResponse;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiTest {

  private Api api;

  @BeforeAll
  public void init() {
    api = new Api("https://testnet.ckb.dev", false);
  }

  @Test
  public void testGetBlockByNumber() throws IOException {
    Block block = api.getBlockByNumber(1);
    Assertions.assertEquals(1, block.transactions.size());
  }

  @Test
  public void testGetBlockHashByNumber() throws IOException {
    byte[] blockHash = api.getBlockHash(1);
    Assertions.assertEquals(
        "0xd5ac7cf8c34a975bf258a34f1c2507638487ab71aa4d10a9ec73704aa3abf9cd",
        Numeric.toHexString(blockHash));
  }

  @Test
  public void testGetBlockEconomicState() throws IOException {
    byte[] blockHash =
        Numeric.hexStringToByteArray(
            "0xd5ac7cf8c34a975bf258a34f1c2507638487ab71aa4d10a9ec73704aa3abf9cd");
    BlockEconomicState blockEconomicState = api.getBlockEconomicState(blockHash);
    Assertions.assertEquals(9207601095L, blockEconomicState.minerReward.secondary);
  }

  @Test
  public void testGetBlock() throws IOException {
    byte[] blockHash =
        Numeric.hexStringToByteArray(
            "0xd5ac7cf8c34a975bf258a34f1c2507638487ab71aa4d10a9ec73704aa3abf9cd");
    Block block = api.getBlock(blockHash);
    Assertions.assertEquals(1, block.transactions.size());
    Assertions.assertNotNull(block.header);
  }

  @Test
  public void testTransaction() throws IOException {
    byte[] transactionHash =
        Numeric.hexStringToByteArray(
            "0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1");
    Transaction transaction = api.getTransaction(transactionHash).transaction;
    Assertions.assertEquals(4, transaction.cellDeps.size());
    Assertions.assertEquals(1, transaction.inputs.size());
    Assertions.assertEquals(3, transaction.outputs.size());
    Assertions.assertEquals(30000000000L, transaction.outputs.get(0).capacity);
  }

  @Test
  public void testGetTipHeader() throws IOException {
    Header header = api.getTipHeader();
    Assertions.assertNotEquals(0, header.number);
    Assertions.assertNotEquals(0, header.compactTarget);
  }

  @Test
  public void testGetTipBlockNumber() throws IOException {
    long blockNumber = api.getTipBlockNumber();
    Assertions.assertNotEquals(0, blockNumber);
  }

  @Test
  public void testGetCurrentEpoch() throws IOException {
    Epoch epoch = api.getCurrentEpoch();
    Assertions.assertNotEquals(0, epoch.number);
    Assertions.assertNotEquals(0, epoch.compactTarget);
  }

  @Test
  public void testGetEpochByNumber() throws IOException {
    Epoch epoch = api.getEpochByNumber(2);
    Assertions.assertEquals(1500, epoch.startNumber);
    Assertions.assertEquals(500945247, epoch.compactTarget);
  }

  @Test
  public void testGetHeader() throws IOException {
    byte[] blockHash =
        Numeric.hexStringToByteArray(
            "0xd5ac7cf8c34a975bf258a34f1c2507638487ab71aa4d10a9ec73704aa3abf9cd");
    Header header = api.getHeader(blockHash);
    Assertions.assertEquals(1, header.number);
    Assertions.assertEquals(1590137711584L, header.timestamp);
  }

  @Test
  public void testGetHeaderByNumber() throws IOException {
    Header header = api.getHeaderByNumber(1);
    Assertions.assertEquals(1, header.number);
    Assertions.assertEquals(1590137711584L, header.timestamp);
  }

  @Test
  public void testGetConsensus() throws IOException {
    Consensus consensus = api.getConsensus();
    Assertions.assertEquals(3500000000L, consensus.maxBlockCycles);
  }

  @Test
  public void testGetBlockMedianTime() throws IOException {
    Long blockMedianTime =
        api.getBlockMedianTime(
            Numeric.hexStringToByteArray(
                "0xd5ac7cf8c34a975bf258a34f1c2507638487ab71aa4d10a9ec73704aa3abf9cd"));
    Assertions.assertNotEquals(0, blockMedianTime);
    Assertions.assertNotEquals(0, blockMedianTime);
  }

  @Test
  public void testGetTransactionProof() throws IOException {
    TransactionProof transactionProof =
        api.getTransactionProof(
            Collections.singletonList(
                Numeric.hexStringToByteArray(
                    "0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1")));
    Assertions.assertNotNull(transactionProof);
    Assertions.assertNotNull(transactionProof.blockHash);
    Assertions.assertEquals(1, transactionProof.proof.indices.size());

    List<byte[]> result = api.verifyTransactionProof(transactionProof);
    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void testGetForkBlock() throws IOException {
    Block forkBlock =
        api.getForkBlock(
            Numeric.hexStringToByteArray(
                "0xd5ac7cf8c34a975bf258a34f1c2507638487ab71aa4d10a9ec73704aa3abf9cd"));
  }

  @Test
  public void testLocalNodeInfo() throws IOException {
    NodeInfo nodeInfo = api.localNodeInfo();
    Assertions.assertTrue(nodeInfo.addresses.size() > 0);
    Assertions.assertTrue(nodeInfo.protocols.size() > 0);
    Assertions.assertTrue(nodeInfo.protocols.get(0).supportVersions.size() > 0);
  }

  @Test
  public void testGetPeers() throws IOException {
    List<PeerNodeInfo> peers = api.getPeers();
    Assertions.assertTrue(peers.size() > 0);
    Assertions.assertTrue(peers.get(0).addresses.size() > 0);
    Assertions.assertTrue(peers.get(0).protocols.size() > 0);
  }

  @Test
  public void testSyncState() throws IOException {
    SyncState state = api.syncState();
    Assertions.assertNotEquals(0, state.bestKnownBlockNumber);
  }

  @Test
  public void testSetNetworkActive() throws IOException {
    api.setNetworkActive(true);
  }

  @Test
  public void testAddNode() throws IOException {
    api.addNode("QmUsZHPbjjzU627UZFt4k8j6ycEcNvXRnVGxCPKqwbAfQS", "/ip4/192.168.2.100/tcp/8114");
  }

  @Test
  public void testRemoveNode() throws IOException {
    api.removeNode("QmUsZHPbjjzU627UZFt4k8j6ycEcNvXRnVGxCPKqwbAfQS");
  }

  @Test
  public void testSetBan() throws IOException {
    BannedAddress bannedAddress =
        new BannedAddress(
            "192.168.0.2", BannedAddress.Command.INSERT, 1840546800000L, true, "test set_ban rpc");
    api.setBan(bannedAddress);
  }

  @Test
  public void testGetBannedAddresses() throws IOException {
    List<BannedResultAddress> bannedAddresses = api.getBannedAddresses();
    Assertions.assertNotNull(bannedAddresses);
  }

  @Test
  public void testClearBannedAddresses() throws IOException {
    api.clearBannedAddresses();
  }

  @Test
  public void testPingPeers() throws IOException {
    api.clearBannedAddresses();
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
    api.clearTxPool();
  }

  @Test
  public void testGetRawTxPool() throws IOException {
    RawTxPool rawTxPool = api.getRawTxPool();
    Assertions.assertNotNull(rawTxPool);
  }

  @Test
  public void testGetRawTxPoolVerbose() throws IOException {
    RawTxPoolVerbose rawTxPoolVerbose = api.getRawTxPoolVerbose();
    Assertions.assertNotNull(rawTxPoolVerbose);

    for (Map.Entry<byte[], RawTxPoolVerbose.VerboseDetail> entry:
        rawTxPoolVerbose.pending.entrySet()) {
      Assertions.assertNotNull((entry.getValue()));
    }

    for (Map.Entry<byte[], RawTxPoolVerbose.VerboseDetail> entry:
        rawTxPoolVerbose.proposed.entrySet()) {
      Assertions.assertNotNull((entry.getValue()));
    }
  }

  @Test
  public void testGetBlockchainInfo() throws IOException {
    BlockchainInfo blockchainInfo = api.getBlockchainInfo();
    Assertions.assertNotNull(blockchainInfo);
  }

  @Test
  public void testGetLiveCell() throws IOException {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint(
                Numeric.hexStringToByteArray(
                    "0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37"),
                0),
            true);
    Assertions.assertNotNull(cellWithStatus.cell);
  }

  @Test
  public void testGetLiveCellWithData() throws IOException {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint(
                Numeric.hexStringToByteArray(
                    "0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37"),
                0),
            true);
    Assertions.assertNotNull(cellWithStatus.cell.data);
  }

  @Test
  public void testGetLiveCellWithoutData() throws IOException {
    CellWithStatus cellWithStatus =
        api.getLiveCell(
            new OutPoint(
                Numeric.hexStringToByteArray(
                    "0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37"),
                0),
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
                    0,
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
                    0,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()),
                OutputsValidator.WELL_KNOWN_SCRIPTS_ONLY);
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
                    0,
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
  void testGetIndexerTip() throws IOException {
    TipResponse tip = api.getIndexerTip();
    Assertions.assertNotNull(tip.blockHash);
    Assertions.assertNotEquals(0, tip.blockNumber);
  }

  @Test
  void testGetTransactions() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
            Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);
    TxsWithCell txs = api.getTransactions(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(txs.objects.size() > 0);
  }

  @Test
  void testTransactionsGrouped() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(Numeric.hexStringToByteArray(
            "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
                   Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
                   Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);

    TxsWithCells txs = api.getTransactionsGrouped(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(txs.objects.size() > 0);
    Assertions.assertNotNull(txs.objects.get(0));
    Assertions.assertNotNull(txs.objects.get(0).cells.get(0));
  }

  @Test
  void testGetCells() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(Numeric.hexStringToByteArray(
            "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
                   Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
                   Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);

    CellsResponse cells = api.getCells(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(cells.objects.size() > 0);
  }

  @Test
  void testGetCellCapacity() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(Numeric.hexStringToByteArray(
            "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
                   Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
                   Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);
    CellCapacityResponse capacity = api.getCellsCapacity(key.build());
    Assertions.assertEquals(1388355000000L, capacity.capacity);
  }

  @Test
  public void testDryRunTransaction() throws IOException {
    Cycles cycles =
        api.dryRunTransaction(
            new Transaction(
                0,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()));
    Assertions.assertNotNull(cycles);
  }

  @Test
  public void testEstimateCycles() throws IOException {
    Cycles cycles =
            api.dryRunTransaction(
                    new Transaction(
                            0,
                            Collections.emptyList(),
                            Collections.emptyList(),
                            Collections.emptyList(),
                            Collections.emptyList(),
                            Collections.emptyList(),
                            Collections.emptyList()));
    Assertions.assertNotNull(cycles);
  }

  @Test
  public void testBatchRpc() throws IOException {
    List<RpcResponse> rpcResponses =
        api.batchRPC(
            Arrays.asList(
                Arrays.asList("get_block_hash", 200),
                Arrays.asList("get_block_by_number", 300),
                Arrays.asList("get_header_by_number", 100)));
    Assertions.assertNotNull(rpcResponses);
    Assertions.assertEquals(3, rpcResponses.size());
    Assertions.assertTrue(rpcResponses.get(0).result instanceof String);
    Assertions.assertTrue(
        GsonFactory.create()
            .fromJson(rpcResponses.get(1).result.toString(), Block.class)
            .transactions.size() > 0);
    Assertions.assertNotNull(
        GsonFactory.create()
            .fromJson(rpcResponses.get(2).result.toString(), Header.class)
            .compactTarget);

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
