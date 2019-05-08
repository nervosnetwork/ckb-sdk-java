package org.nervos.ckb;

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
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;

/** Created by duanyytop on 2019-04-24. Copyright © 2019 Nervos Foundation. All rights reserved. */
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
  public void localNodeInfo() throws IOException {
    NodeInfo nodeInfo = ckbService.localNodeInfo().send().getNodeInfo();
    Assertions.assertNotNull(nodeInfo);
  }

  @Test
  public void testGetCellsByLockHash() throws IOException {
    List<CellOutputWithOutPoint> cellOutputWithOutPoints =
        ckbService
            .getCellsByLockHash(
                "0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff", "0", "100")
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
                    "0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff", 0))
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
                    0,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList()))
            .send()
            .getTransactionHash();
    Assertions.assertNotNull(transactionHash);
  }
}
