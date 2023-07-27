package service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.service.RpcResponse;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.ScriptSearchMode;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.*;

import java.io.IOException;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiTest {
  // python code:    "block_hash_that_does_not_exist".encode("utf-8").hex()
  // output   '626c6f636b5f686173685f746861745f646f65735f6e6f745f6578697374'
  byte[] BLOCK_HASH_NOT_EXIST = Numeric.hexStringToByteArray(
      "0x626c6f636b5f686173685f746861745f646f65735f6e6f745f65786973740000");
  long block_number_not_exist = 0xffffffffffffffffL;

  private Api api;

  @BeforeAll
  public void init() {
    api = new Api("https://testnet.ckb.dev", false);
  }

  @Test
  public void testNoTrailingNullParams() {
    Assertions.assertEquals(Arrays.asList(1, 2), Api.noTrailingNullParams(1, 2, null));
    Assertions.assertEquals(Arrays.asList(1, 2), Api.noTrailingNullParams(1, 2, null, null));
    Assertions.assertEquals(Arrays.asList(1, 2, null, 3), Api.noTrailingNullParams(1, 2, null, 3));
    Assertions.assertEquals(Collections.emptyList(), Api.noTrailingNullParams((Object) null));
    Assertions.assertEquals(Collections.emptyList(), Api.noTrailingNullParams(null, null));
  }

  @Test
  public void testGetBlockByNumber() throws IOException {
    Block block = api.getBlockByNumber(1);
    Assertions.assertEquals(1, block.transactions.size());
  }

  @Test
  public void testGetBlockByNumberWithCycles() throws IOException {
    long blockNumber = 7981482;
    BlockWithCycles response = api.getBlockByNumber(blockNumber, true);
    Assertions.assertEquals(response.cycles.size() + 1, response.block.transactions.size());
    Assertions.assertTrue(response.cycles.size() > 0);

    BlockWithCycles response0 = api.getBlockByNumber(blockNumber, false);
    Assertions.assertArrayEquals(response0.block.pack().toByteArray(), response.block.pack().toByteArray());
    Assertions.assertNull(response0.cycles);

    PackedBlockWithCycles packedResponse = api.getPackedBlockByNumber(blockNumber, true);
    Assertions.assertEquals(Numeric.toHexString(packedResponse.getBlockBytes()), Numeric.toHexString(response.block.pack().toByteArray()));
    Assertions.assertArrayEquals(packedResponse.getBlockBytes(), response.block.pack().toByteArray());
    Assertions.assertEquals(response.cycles, packedResponse.cycles);

    PackedBlockWithCycles packedResponse0 = api.getPackedBlockByNumber(blockNumber, false);
    Assertions.assertArrayEquals(packedResponse.getBlockBytes(), packedResponse0.getBlockBytes());
    Assertions.assertNull(packedResponse0.cycles);
  }

  @Test
  public void testGetBlockByNumberWithCycles_NotExist() throws IOException {
    long blockNumber = block_number_not_exist;
    BlockWithCycles response = api.getBlockByNumber(blockNumber, true);
    Assertions.assertNull(response);

    BlockWithCycles response0 = api.getBlockByNumber(blockNumber, false);
    Assertions.assertNull(response0);

    PackedBlockWithCycles packedResponse = api.getPackedBlockByNumber(blockNumber, true);
    Assertions.assertNull(packedResponse);

    PackedBlockWithCycles packedResponse0 = api.getPackedBlockByNumber(blockNumber, false);
    Assertions.assertNull(packedResponse0);
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

    PackedBlockWithCycles packedBlockBytes = api.getPackedBlock(blockHash, true);
    byte[] bytes = packedBlockBytes.getBlockBytes();
    Assertions.assertNotNull(bytes);
    Assertions.assertNotNull(packedBlockBytes.cycles);

    PackedBlockWithCycles packedBlockBytes0 = api.getPackedBlock(blockHash, false);
    Assertions.assertNull(packedBlockBytes0.cycles);
    Assertions.assertEquals(packedBlockBytes.block, packedBlockBytes0.block);

    org.nervos.ckb.type.concrete.Block block_from_molecule = org.nervos.ckb.type.concrete.Block.builder(bytes).build();

    org.nervos.ckb.type.concrete.Block block_from_json = block.pack();

    byte[] bytes_from_json = block_from_json.toByteArray();
    Assertions.assertEquals(Numeric.toHexString(bytes), Numeric.toHexString(bytes_from_json));
    Assertions.assertArrayEquals(bytes, bytes_from_json);
  }

  @Test
  public void testGetPackedBlock_NotExist() throws IOException {
    byte[] blockHash = BLOCK_HASH_NOT_EXIST;
    Block block = api.getBlock(blockHash);
    Assertions.assertNull(block);

    PackedBlockWithCycles packedBlockBytes = api.getPackedBlock(blockHash, true);
    Assertions.assertNull(packedBlockBytes);

    PackedBlockWithCycles packedBlockBytes0 = api.getPackedBlock(blockHash, false);
    Assertions.assertNull(packedBlockBytes0);
  }

  @Test
  public void testGetBlockWithCycles() throws IOException {
    byte[] blockHash =
        Numeric.hexStringToByteArray(
            "0xd88eb0cf9f6e6f123c733e9aba29dec9cb449965a8adc98216c50d5083b909b1");
    BlockWithCycles response = api.getBlock(blockHash, true);
    Assertions.assertEquals(response.cycles.size() + 1, response.block.transactions.size());
    Assertions.assertTrue(response.cycles.size() > 0);
    Assertions.assertNotNull(response.block.header);

    BlockWithCycles response0 = api.getBlock(blockHash, false);
    Assertions.assertArrayEquals(response0.block.pack().toByteArray(), response.block.pack().toByteArray());
    Assertions.assertNull(response0.cycles);
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

    transactionHash =
        Numeric.hexStringToByteArray(
            "0x3dca00e45e2f3a39d707d5559ba49d27d21038624b0402039898d3a8830525be");
    TransactionWithStatus transactionWithStatus = api.getTransaction(transactionHash);

    Assertions.assertNotNull(transactionWithStatus.txStatus);
    Assertions.assertNotNull(transactionWithStatus.cycles);
    Assertions.assertTrue(transactionWithStatus.cycles > 0);
  }

  @Test
  public void testGetTransactionVerbosity1() throws IOException {
    byte[] transactionHash =
        Numeric.hexStringToByteArray(
            "0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1");
    TransactionWithStatus transactionVerbosity1 = api.getTransactionStatus(transactionHash);
    Assertions.assertNull(transactionVerbosity1.transaction);

    TransactionWithStatus transactionVerbosity2 = api.getTransaction(transactionHash);
    Assertions.assertEquals(transactionVerbosity1.txStatus.status, transactionVerbosity2.txStatus.status);
    Assertions.assertArrayEquals(transactionVerbosity1.txStatus.blockHash, transactionVerbosity2.txStatus.blockHash);
    Assertions.assertEquals(transactionVerbosity1.cycles, transactionVerbosity2.cycles);
  }

  @Test
  public void testGetTransactionVerbosity1_NotExist() throws IOException {
    byte[] transactionHash = BLOCK_HASH_NOT_EXIST;
    TransactionWithStatus transactionVerbosity1 = api.getTransactionStatus(transactionHash);
    Assertions.assertEquals(TransactionWithStatus.Status.UNKNOWN, transactionVerbosity1.txStatus.status);

    TransactionWithStatus transactionVerbosity2 = api.getTransaction(transactionHash);
    Assertions.assertEquals(TransactionWithStatus.Status.UNKNOWN, transactionVerbosity1.txStatus.status);
  }

  @Test
  public void testPackedTransaction() throws IOException {
    byte[] transactionHash =
        Numeric.hexStringToByteArray(
            "0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1");
    byte[] transaction_bytes = api.getPackedTransaction(transactionHash).getTransactionBytes();

    Transaction transaction = api.getTransaction(transactionHash).transaction;
    byte[] bytes_from_json = transaction.pack().toByteArray();
    Assertions.assertArrayEquals(bytes_from_json, transaction_bytes);
  }

  @Test
  public void testPackedTransactionNotExist() throws IOException {
    byte[] transactionHash = BLOCK_HASH_NOT_EXIST;
    PackedTransactionWithStatus packedTransaction = api.getPackedTransaction(transactionHash);
    Assertions.assertEquals(TransactionWithStatus.Status.UNKNOWN, packedTransaction.txStatus.status);

    TransactionWithStatus transaction = api.getTransaction(transactionHash);
    Assertions.assertEquals(TransactionWithStatus.Status.UNKNOWN, transaction.txStatus.status);
  }

  @Test
  public void testGetTipHeader() throws IOException {
    Header header = api.getTipHeader();
    Assertions.assertNotEquals(0, header.number);
    Assertions.assertNotEquals(0, header.compactTarget);
  }

  @Test
  public void testGetPackedTipHeader() throws IOException {
    PackedHeader tipHeader = api.getPackedTipHeader();

    org.nervos.ckb.type.concrete.Header h = org.nervos.ckb.type.concrete.Header.builder(tipHeader.getHeaderBytes()).build();

    byte[] headerHash = tipHeader.calculateHash();
    PackedHeader packedHeader = api.getPackedHeader(headerHash);
    Assertions.assertEquals(tipHeader.header, packedHeader.header);
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

    PackedHeader packedHeader = api.getPackedHeader(blockHash);
    Assertions.assertArrayEquals(header.pack().toByteArray(), packedHeader.getHeaderBytes());
  }

  @Test
  public void testGetHeader_NotExist() throws IOException {
    byte[] blockHash = BLOCK_HASH_NOT_EXIST;
    Header header = api.getHeader(blockHash);
    Assertions.assertNull(header);

    PackedHeader packedHeader = api.getPackedHeader(blockHash);
    Assertions.assertNull(packedHeader);
  }

  @Test
  public void testGetHeaderByNumber() throws IOException {
    Header header = api.getHeaderByNumber(1);
    Assertions.assertEquals(1, header.number);
    Assertions.assertEquals(1590137711584L, header.timestamp);

    PackedHeader packedHeader = api.getPackedHeaderByNumber(1);
    Assertions.assertArrayEquals(header.pack().toByteArray(), packedHeader.getHeaderBytes());
  }

  @Test
  public void testGetHeaderByNumber_NotExist() throws IOException {
    Header header = api.getHeaderByNumber(block_number_not_exist);
    Assertions.assertNull(header);

    PackedHeader packedHeader = api.getPackedHeaderByNumber(block_number_not_exist);
    Assertions.assertNull(packedHeader);
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
  public void testGetTransactionAndWitnessProof() throws IOException {
    List<byte[]> txHashes = Collections.singletonList(
        Numeric.hexStringToByteArray("0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1"));
    TransactionAndWitnessProof proof = api.getTransactionAndWitnessProof(txHashes, null);
    Assertions.assertNotNull(proof);
    Assertions.assertNotNull(proof.blockHash);
    Assertions.assertEquals(1, proof.transactionsProof.indices.size());
    Assertions.assertEquals(proof.witnessesProof.indices.size(), proof.transactionsProof.indices.size());

    TransactionAndWitnessProof proof2 = api.getTransactionAndWitnessProof(txHashes, proof.blockHash);
    Assertions.assertEquals(proof, proof2);

    List<byte[]> result = api.verifyTransactionAndWitnessProof(proof);

    Assertions.assertNotNull(result);
    Iterator<byte[]> l_it = txHashes.iterator();
    Iterator<byte[]> r_it = result.iterator();
    while (l_it.hasNext() && r_it.hasNext()) {
      Assertions.assertArrayEquals(l_it.next(), r_it.next());
    }
    Assertions.assertFalse(l_it.hasNext());
    Assertions.assertFalse(r_it.hasNext());
  }

  @Test
  public void testGetForkBlock() throws IOException {
    byte[] block_hash = Numeric.hexStringToByteArray(
        "0xd5ac7cf8c34a975bf258a34f1c2507638487ab71aa4d10a9ec73704aa3abf9cd");
    Block forkBlock = api.getForkBlock(block_hash);

    PackedBlockWithCycles packedForkBlock = api.getPackedForkBlock(block_hash);
    if (packedForkBlock != null) {
      Assertions.assertArrayEquals(packedForkBlock.getBlockBytes(), forkBlock.pack().toByteArray());
    }
  }

  @Test
  public void testGetForkBlock_NotExist() throws IOException {
    byte[] block_hash = BLOCK_HASH_NOT_EXIST;
    Block forkBlock = api.getForkBlock(block_hash);
    Assertions.assertNull(forkBlock);

    PackedBlockWithCycles packedForkBlock = api.getPackedForkBlock(block_hash);
    Assertions.assertNull(packedForkBlock);
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

  @Disabled
  @Test
  public void testSetNetworkActive() throws IOException {
    api.setNetworkActive(true);
  }

  @Disabled
  @Test
  public void testAddNode() throws IOException {
    api.addNode("QmUsZHPbjjzU627UZFt4k8j6ycEcNvXRnVGxCPKqwbAfQS", "/ip4/192.168.2.100/tcp/8114");
  }

  @Disabled
  @Test
  public void testRemoveNode() throws IOException {
    api.removeNode("QmUsZHPbjjzU627UZFt4k8j6ycEcNvXRnVGxCPKqwbAfQS");
  }

  @Disabled
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

  @Disabled
  @Test
  public void testClearBannedAddresses() throws IOException {
    api.clearBannedAddresses();
  }

  @Disabled
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

  @Disabled
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

    for (Map.Entry<byte[], RawTxPoolVerbose.VerboseDetail> entry :
        rawTxPoolVerbose.pending.entrySet()) {
      Assertions.assertNotNull((entry.getValue()));
    }

    for (Map.Entry<byte[], RawTxPoolVerbose.VerboseDetail> entry :
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
  void testGetTransactions_prefix_partial() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
            Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae".substring(0, 4)),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK);
    key.scriptSearchMode(ScriptSearchMode.Prefix);
    TxsWithCell txs = api.getTransactions(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(txs.objects.size() > 0);
  }

  @Test
  void testGetTransactions_exact_partial() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
            Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae".substring(0, 4)),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK).scriptSearchMode(ScriptSearchMode.Exact);
    TxsWithCell txs = api.getTransactions(key.build(), Order.ASC, 10, null);
    Assertions.assertEquals(0, txs.objects.size());
  }

  @Test
  void testGetTransactions_exact_full() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(
        new Script(
            Numeric.hexStringToByteArray(
                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
            Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
            Script.HashType.TYPE));
    key.scriptType(ScriptType.LOCK).scriptSearchMode(ScriptSearchMode.Exact);
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
        api.estimateCycles(
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

  @Test
  public void testGetFeeRateStatics() throws IOException {
    FeeRateStatistics statistics = api.getFeeRateStatistics(null);
    Assertions.assertNotNull(statistics);
    Assertions.assertTrue(statistics.mean > 0 && statistics.median > 0);

    statistics = api.getFeeRateStatistics(1);
    Assertions.assertTrue(statistics == null || statistics.mean > 0 && statistics.median > 0);

    statistics = api.getFeeRateStatistics(101);
    Assertions.assertTrue(statistics == null || statistics.mean > 0 && statistics.median > 0);

    statistics = api.getFeeRateStatistics(0);
    Assertions.assertTrue(statistics == null || statistics.mean > 0 && statistics.median > 0);

    statistics = api.getFeeRateStatistics(102);
    Assertions.assertTrue(statistics == null || statistics.mean > 0 && statistics.median > 0);
  }
}
