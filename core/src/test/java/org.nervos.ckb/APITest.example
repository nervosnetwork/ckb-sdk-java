package org.nervos.ckb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nervos.ckb.response.item.Block;
import org.nervos.ckb.response.item.Cell;
import org.nervos.ckb.response.item.Header;
import org.nervos.ckb.response.item.Transaction;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * Created by duanyytop on 2018-12-25.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class APITest {

    private static final String NODE_URL = "http://localhost:8114/";

    private static CKBService ckbService;

    @Before
    public void initService() {
        HttpService.setDebug(false);
        ckbService = CKBService.build(new HttpService(NODE_URL));
    }

    @Test
    public void testBlockAndTransaction() throws IOException {
        String blockHash = ckbService.getBlockHash(1).send().getBlockHash();
        Assert.assertNotNull("Block hash is null", blockHash);

        Block block = ckbService.getBlock(blockHash).send().getBlock();
        Assert.assertNotNull("Block is null", block);
        Assert.assertNotNull("Block header is null", block.header);

        String transactionHash = block.transactions.get(0).hash;
        Transaction transaction = ckbService.getTransaction(transactionHash).send().getTransaction().transaction;
        Assert.assertNotNull("Transaction is null", transaction);

    }

    @Test
    public void testGetTipHeader() throws IOException {
        Header header = ckbService.getTipHeader().send().getHeader();
        Assert.assertNotNull("Tip header is null", header);
    }

    @Test
    public void testGetTipBlockNumber() throws IOException {
        BigInteger blockNumber = ckbService.getTipBlockNumber().send().getBlockNumber();
        Assert.assertNotNull("Block number is null", blockNumber.toString());
    }

    @Test
    public void testLocalNodeId() throws IOException {
        String nodeId = ckbService.localNodeId().send().getNodeId();
        Assert.assertNotNull("Node id is null", nodeId);
    }

    @Test
    public void testGetCellsByTypeHash() throws IOException {
        List<Cell> cells = ckbService.getCellsByTypeHash(
                "0xcf7294651a9e2033243b04cfd3fa35097d56b811824691a75cd29d50ac23720a", 1, 100
        ).send().getCells();
        Assert.assertTrue("Cells is null", cells.size() > 0);
    }

    @Test
    public void testGetCurrentCell() throws IOException {
        Cell cell = ckbService.getCurrentCell(
                new Cell.OutPoint("0x10262d4d6918774ae939a55b88f1b2c847f75489e36cd58cca03bad5c216db4f", 0)
        ).send().getCell();
        Assert.assertNotNull("Cell is null", cell);
    }

    @Test
    public void testSendTransaction() throws IOException {
        String transactionHash = ckbService.sendTransaction(
               new Transaction(0, Collections.emptyList(), Collections.emptyList(), Collections.emptyList())
        ).send().getTransactionHash();
        Assert.assertNotNull("Transaction hash is null", transactionHash);
    }


}
