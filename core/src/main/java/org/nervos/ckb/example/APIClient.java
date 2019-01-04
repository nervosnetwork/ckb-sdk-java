package org.nervos.ckb.example;

import com.google.gson.Gson;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.exceptions.APIErrorException;
import org.nervos.ckb.response.item.*;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.service.CKBService;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class APIClient {

    private static final String NODE_URL = "http://localhost:8114/";

    private static CKBService ckbService;

    private static Gson gson = new Gson();


    static {
        HttpService.setDebug(false);
        ckbService = CKBService.build(new HttpService(NODE_URL));
    }

    public static void main(String[] args) throws IOException {

        long blockNumber = 0;

        String blockHash = getBlockHash(blockNumber);
        System.out.println("Block hash: " + blockHash);

        Block block = getBlock(blockHash);
        System.out.println("Block: " + gson.toJson(block));

        System.out.println("Transaction: " + gson.toJson(getTransaction(block.commitTransactions.get(0).hash)));

        System.out.println("Header: " + gson.toJson(getTipHeader()));

        System.out.println("Block number: " + getTipBlockNumber().toString());

        System.out.println("Local host: " + localNodeId());

        System.out.println("Cells: " + gson.toJson(getCellsByTypeHash()));

        System.out.println("Cell: " + gson.toJson(getLiveCell()));

        System.out.println("Transaction hash: " + sendTransaction());

        System.out.println("Always Success Cell Hash: " + alwaysSuccessCellHash());

        System.out.println("Always Success Script OutPoint: " + gson.toJson(alwaysSuccessScriptOutPoint()));

    }

    private static String getBlockHash(long blockNumber) throws IOException {
        return ckbService.getBlockHash(blockNumber).send().getBlockHash();
    }

    private static Block getBlock(String blockHash) throws IOException {
        return ckbService.getBlock(blockHash).send().getBlock();
    }

    private static Transaction getTransaction(String transactionHash) throws IOException {
        return ckbService.getTransaction(transactionHash).send().getTransaction();
    }

    private static Header getTipHeader() throws IOException {
        return ckbService.getTipHeader().send().getHeader();
    }


    private static BigInteger getTipBlockNumber() throws IOException {
        return ckbService.getTipBlockNumber().send().getBlockNumber();
    }


    private static String localNodeId() throws IOException {
        return ckbService.localNodeId().send().getNodeId();
    }


    private static List<Cell> getCellsByTypeHash() throws IOException {
        return ckbService.getCellsByTypeHash(
                "0xcf7294651a9e2033243b04cfd3fa35097d56b811824691a75cd29d50ac23720a", 1, 100
        ).send().getCells();
    }

    private static Cell getLiveCell() throws IOException {
        return ckbService.getLiveCell(
                new Cell.OutPoint("0x10262d4d6918774ae939a55b88f1b2c847f75489e36cd58cca03bad5c216db4f", 0)
        ).send().getCell();
    }


    private static String sendTransaction() throws IOException {
        return ckbService.sendTransaction(
                new Transaction(0, Collections.emptyList(), Collections.emptyList(), Collections.emptyList())
        ).send().getTransactionHash();
    }


    private static Block genesisBlock() throws IOException {
        String blockHash = ckbService.getBlockHash(0).send().getBlockHash();
        return ckbService.getBlock(blockHash).send().getBlock();
    }


    private static String alwaysSuccessCellHash() throws IOException {
        List<Output> systemCells = genesisBlock().commitTransactions.get(0).outputs;
        if (systemCells.isEmpty() || systemCells.get(0) == null) {
            throw new APIErrorException("Cannot find always success cell");
        }
        return Hash.sha3(systemCells.get(0).data);
    }


    private static Cell.OutPoint alwaysSuccessScriptOutPoint() throws IOException {
        String hash = genesisBlock().commitTransactions.get(0).hash;
        return new Cell.OutPoint(hash, 0);
    }

}
