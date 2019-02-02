package org.nervos.ckb.rpc;

import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.exceptions.APIErrorException;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.wallet.Constant;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-31.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class RpcRequest {

    private static CKBService ckbService;

    static {
        HttpService.setDebug(false);
        ckbService = CKBService.build(new HttpService(Constant.NODE_URL));
    }

    public static String getBlockHash(long blockNumber) throws IOException {
        return ckbService.getBlockHash(blockNumber).send().getBlockHash();
    }

    public static Block getBlock(String blockHash) throws IOException {
        return ckbService.getBlock(blockHash).send().getBlock();
    }

    public static Transaction getTransaction(String transactionHash) throws IOException {
        return ckbService.getTransaction(transactionHash).send().getTransaction();
    }

    public static Header getTipHeader() throws IOException {
        return ckbService.getTipHeader().send().getHeader();
    }


    public static BigInteger getTipBlockNumber() throws IOException {
        return ckbService.getTipBlockNumber().send().getBlockNumber();
    }


    public static String localNodeId() throws IOException {
        return ckbService.localNodeId().send().getNodeId();
    }

    public static List<Cell> getCellsByTypeHash(String typeHash, long fromBlockNumber, long toBlockNumber) throws IOException {
        return ckbService.getCellsByTypeHash(typeHash, fromBlockNumber, toBlockNumber).send().getCells();
    }

    public static Cell getLiveCell() throws IOException {
        return ckbService.getLiveCell(
                new OutPoint("0x15c809f08c7bca63d2b661e1dbc26c74551a6f982f7631c718dc43bd2bb5c90e", 0)
        ).send().getCellStatus();
    }


    public static String sendTransaction(Transaction transaction) throws IOException {
        return ckbService.sendTransaction(transaction).send().getTransactionHash();
    }


    public static Block genesisBlock() throws IOException {
        String blockHash = ckbService.getBlockHash(0).send().getBlockHash();
        return ckbService.getBlock(blockHash).send().getBlock();
    }


    public static String alwaysSuccessCellHash() throws IOException {
        List<Output> systemCells = genesisBlock().commitTransactions.get(0).outputs;
        if (systemCells.isEmpty() || systemCells.get(0) == null) {
            throw new APIErrorException("Cannot find always success cell");
        }
        return Hash.sha3(systemCells.get(0).data);
    }

    public static List<OutPoint> alwaysSuccessScriptOutPoint() throws IOException {
        String hash = genesisBlock().commitTransactions.get(0).hash;
        return Collections.singletonList(new OutPoint(hash, 0));
    }

}
