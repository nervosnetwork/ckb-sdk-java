package org.nervos.ckb.example;

import com.google.gson.Gson;
import org.nervos.ckb.response.item.Block;
import org.nervos.ckb.response.item.Cell;
import org.nervos.ckb.response.item.Header;
import org.nervos.ckb.response.item.Transaction;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.service.CKBService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class APIClient {

    private static final String NODE_URL = "http://localhost:8114/";

    private static CKBService ckbService;


    static {
        HttpService.setDebug(false);
        ckbService = CKBService.build(new HttpService(NODE_URL));
    }

    public static void main(String[] args) throws IOException {

        String blockHash = ckbService.getBlockHash(1).send().getBlockHash();
        System.out.println("Second block hash is " + blockHash);

        Block block = ckbService.getBlock(blockHash).send().getBlock();
        System.out.println("Second block is " + new Gson().toJson(block));

        String transactionHash = block.transactions.get(0).hash;
        Transaction transaction = ckbService.getTransaction(transactionHash).send().getTransaction().transaction;
        System.out.println("The result of getTransactionByHash is " + new Gson().toJson(transaction));

        Header header = ckbService.getTipHeader().send().getHeader();
        System.out.println("The tip header is " + new Gson().toJson(header));

        BigInteger blockNumber = ckbService.getTipBlockNumber().send().getBlockNumber();
        System.out.println("The result of getTipBlockNumber is " + blockNumber.toString());

        String nodeId = ckbService.localNodeId().send().getNodeId();
        System.out.println("Local node id is " + nodeId);

        List<Cell> cells = ckbService.getCellsByTypeHash(
                "0xcf7294651a9e2033243b04cfd3fa35097d56b811824691a75cd29d50ac23720a", 1, 100
        ).send().getCells();
        System.out.println("The result of getCellsByTypeHash is " + new Gson().toJson(cells));

        Cell cell = ckbService.getCurrentCell(
                new Cell.OutPoint("0x10262d4d6918774ae939a55b88f1b2c847f75489e36cd58cca03bad5c216db4f", 0)
        ).send().getCell();
        System.out.println("Current cell is " + new Gson().toJson(cell));

        String hash = ckbService.sendTransaction(
                new Transaction(0, Collections.emptyList(), Collections.emptyList(), Collections.emptyList())
        ).send().getTransactionHash();
        System.out.println("The result transaction hash of sending transaction is " + hash);

    }

}
