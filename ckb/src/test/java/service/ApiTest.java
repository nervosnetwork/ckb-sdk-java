package service;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.RpcResponse;
import org.nervos.ckb.type.*;
import org.nervos.ckb.type.cell.CellWithStatus;
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
  public void testGetConsensus() throws IOException {
    Consensus consensus = api.getConsensus();
    Assertions.assertNotNull(consensus);
    Assertions.assertNotNull(consensus.blockVersion);
    Assertions.assertNotNull(consensus.proposerRewardRatio.denom);
  }

  @Test
  public void testGetTransactionProof() throws IOException {
    TransactionProof transactionProof =
        api.getTransactionProof(
            Collections.singletonList(
                "0xa4037a893eb48e18ed4ef61034ce26eba9c585f15c9cee102ae58505565eccc3"));
    Assertions.assertNotNull(transactionProof);
    Assertions.assertNotNull(transactionProof.blockHash);
    Assertions.assertTrue(transactionProof.proof.indices.size() > 0);
  }

  @Test
  public void testVerifyTransactionProof() throws IOException {
    TransactionProof transactionProof =
        new TransactionProof(
            new TransactionProof.Proof(Collections.singletonList("0x"), Collections.emptyList()),
            "0x7978ec7ce5b507cfb52e149e36b1a23f6062ed150503c85bbf825da3599095ed",
            "0x2bb631f4a251ec39d943cc238fc1e39c7f0e99776e8a1e7be28a03c70c4f4853");
    String result = api.verifyTransactionProof(transactionProof);
    Assertions.assertNotNull(result);
  }

  @Test
  public void testLocalNodeInfo() throws IOException {
    NodeInfo nodeInfo = api.localNodeInfo();
    Assertions.assertNotNull(nodeInfo);
    Assertions.assertTrue(nodeInfo.addresses.size() > 0);
    Assertions.assertTrue(nodeInfo.protocols.size() > 0);
    Assertions.assertTrue(nodeInfo.protocols.get(0).supportVersions.size() > 0);
  }

  @Test
  public void testGetPeers() throws IOException {
    List<PeerNodeInfo> peers = api.getPeers();
    Assertions.assertNotNull(peers);
    Assertions.assertTrue(peers.size() > 0);
    Assertions.assertTrue(peers.get(0).addresses.size() > 0);
    Assertions.assertTrue(peers.get(0).protocols.size() > 0);
    Assertions.assertNotNull(peers.get(0).protocols.get(0).version);
  }

  @Test
  public void testSyncState() throws IOException {
    SyncState state = api.syncState();
    Assertions.assertNotNull(state);
    Assertions.assertNotNull(state.bestKnownBlockNumber);
  }

  @Test
  public void testSetNetworkActive() throws IOException {
    String result = api.setNetworkActive(true);
    Assertions.assertNull(result);
  }

  @Test
  public void testAddNode() throws IOException {
    String result =
        api.addNode(
            "QmUsZHPbjjzU627UZFt4k8j6ycEcNvXRnVGxCPKqwbAfQS", "/ip4/192.168.2.100/tcp/8114");
    Assertions.assertNull(result);
  }

  @Test
  public void testRemoveNode() throws IOException {
    String result = api.removeNode("QmUsZHPbjjzU627UZFt4k8j6ycEcNvXRnVGxCPKqwbAfQS");
    Assertions.assertNull(result);
  }

  @Test
  public void testSetBan() throws IOException {
    BannedAddress bannedAddress =
        new BannedAddress("192.168.0.2", "insert", "1840546800000", true, "test set_ban rpc");
    String banResult = api.setBan(bannedAddress);
    Assertions.assertNull(banResult);
  }

  @Test
  public void testGetBannedAddresses() throws IOException {
    List<BannedResultAddress> bannedAddresses = api.getBannedAddresses();
    Assertions.assertNotNull(bannedAddresses);
  }

  @Test
  public void testClearBannedAddresses() throws IOException {
    String result = api.clearBannedAddresses();
    Assertions.assertNull(result);
  }

  @Test
  public void testPingPeers() throws IOException {
    String result = api.clearBannedAddresses();
    Assertions.assertNull(result);
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
  public void testGetRawTxPool() throws IOException {
    RawTxPool rawTxPool = api.getRawTxPool();
    Assertions.assertNotNull(rawTxPool);
  }

  @Test
  public void testGetRawTxPoolVerbose() throws IOException {
    RawTxPoolVerbose rawTxPoolVerbose = api.getRawTxPoolVerbose();
    Assertions.assertNotNull(rawTxPoolVerbose);

    for (Map.Entry<String, RawTxPoolVerbose.VerboseDetail> entry :
        rawTxPoolVerbose.pending.entrySet()) {
      Assertions.assertNotNull((entry.getValue()));
    }

    for (Map.Entry<String, RawTxPoolVerbose.VerboseDetail> entry :
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
