package com.nervos.lightclient;

import com.nervos.lightclient.type.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.CellCapacityResponse;
import org.nervos.indexer.model.resp.CellsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
class DefaultLightClientApiTest {
  public LightClientApi api = new DefaultLightClientApi("http://localhost:9000");
  // ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r
  public Script script = new Script(
      Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
      Numeric.hexStringToByteArray("0x4049ed9cec8a0d39c7a1e899f0dacb8a8c28ad14"),
      Script.HashType.TYPE);

  @Test
  void setScripts() throws IOException {
    ScriptDetail scriptDetail = new ScriptDetail();
    scriptDetail.script = script;
    scriptDetail.scriptType = ScriptType.LOCK;
    scriptDetail.blockNumber = 7033100;
    List<ScriptDetail> scriptDetails = new ArrayList<>();
    scriptDetails.add(scriptDetail);
    api.setScripts(scriptDetails);
  }

  @Test
  void getScripts() throws IOException {
    List<ScriptDetail> scriptDetails = api.getScripts();
    Assertions.assertTrue(scriptDetails.size() > 0);
    Assertions.assertTrue(scriptDetails.stream().anyMatch(scriptDetail -> scriptDetail.script != null));
    Assertions.assertTrue(scriptDetails.stream().anyMatch(scriptDetail -> scriptDetail.blockNumber > 0));
    Assertions.assertTrue(scriptDetails.stream().anyMatch(scriptDetail -> scriptDetail.scriptType != null));
  }

  @Test
  void getTipHeader() throws IOException {
    Header header = api.getTipHeader();
    Assertions.assertNotNull(header);
  }

  @Test
  void getGenesisBlock() throws IOException {
    Block block = api.getGenesisBlock();
    Assertions.assertNotNull(block);
  }

  @Test
  void getHeader() throws IOException {
    Header header = api.getHeader(Numeric.hexStringToByteArray("0xc78c65185c14e1b02d6457a06b4678bab7e15f194f49a840319b57c67d20053c"));
    Assertions.assertNotNull(header);
  }

  @Test
  void getTransaction() throws IOException {
    TransactionWithHeader tx = api.getTransaction(Numeric.hexStringToByteArray("0x151d4d450c9e3bccf4b47d1ba6942d4e9c8c0eeeb7b9f708df827c164f035aa8"));
    Assertions.assertNotNull(tx.header);
    Assertions.assertNotNull(tx.transaction);
  }

  @Test
  void fetchHeader() throws IOException {
    FetchedHeader header = api.fetchHeader(Numeric.hexStringToByteArray("0xcb5eae958e3ea24b0486a393133aa33d51224ffaab3c4819350095b3446e4f70"));
    Assertions.assertNotNull(header.status);
    Assertions.assertNotNull(header.data);
  }

  @Test
  void fetchTransaction() throws IOException {
    FetchedTransaction tx = api.fetchTransaction(Numeric.hexStringToByteArray("0x716e211698d3d9499aae7903867c744b67b539beeceddad330e73d1b6b617aef"));
    Assertions.assertNotNull(tx.status);
    Assertions.assertNotNull(tx.data);
  }

  @Test
  void getCells() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(script);
    key.scriptType(ScriptType.LOCK);

    CellsResponse cells = api.getCells(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(cells.objects.size() > 0);
    Assertions.assertTrue(cells.objects.stream().anyMatch(obj -> obj.blockNumber != 0));
    Assertions.assertTrue(cells.objects.stream().anyMatch(obj -> obj.txIndex != 0));
    Assertions.assertTrue(cells.objects.stream().anyMatch(obj -> obj.outPoint != null));
    Assertions.assertTrue(cells.objects.stream().anyMatch(obj -> obj.output != null));
  }

  @Test
  void getTransactions() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(script);
    key.scriptType(ScriptType.LOCK);

    TxsWithCell txs = api.getTransactions(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(txs.objects.size() > 0);
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.transaction != null));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.blockNumber != 0));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.ioIndex != 0));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.ioType != null));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.txIndex != 0));
  }

  @Test
  void getTransactionsGrouped() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(script);
    key.scriptType(ScriptType.LOCK);

    TxsWithCells txs = api.getTransactionsGrouped(key.build(), Order.ASC, 10, null);
    Assertions.assertTrue(txs.objects.size() > 0);
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.blockNumber != 0));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.cells.size() > 0));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.cells.get(0) != null));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.cells.size() >= 2 && obj.cells.get(1).ioIndex != 0));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.txIndex != 0));
    Assertions.assertTrue(txs.objects.stream().anyMatch(obj -> obj.transaction != null));
  }

  @Test
  void getCellsCapacity() throws IOException {
    SearchKeyBuilder key = new SearchKeyBuilder();
    key.script(script);
    key.scriptType(ScriptType.LOCK);
    CellCapacityResponse capacity = api.getCellsCapacity(key.build());
    Assertions.assertNotEquals(0L, capacity.capacity);
    Assertions.assertNotEquals(0L, capacity.blockNumber);
    Assertions.assertNotNull(capacity.blockHash);
  }
}
