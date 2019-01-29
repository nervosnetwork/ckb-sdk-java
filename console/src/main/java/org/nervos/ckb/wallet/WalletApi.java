package org.nervos.ckb.wallet;

import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.exceptions.APIErrorException;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class WalletApi {

    private static CKBService ckbService;

    static {
        ckbService = CKBService.build(new HttpService(Constant.NODE_URL));
    }


    public static List<Cell> getUnspendCells() {
        List<Cell> results = new ArrayList<>();
        try {
            long toBlockNumber = ckbService.getTipBlockNumber().send().getBlockNumber().longValue();
            long fromBlockNumber = 1;
            while (fromBlockNumber <= toBlockNumber) {
                long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
                List<Cell> cells = ckbService.getCellsByTypeHash(getAddress(), fromBlockNumber, currentToBlockNumber).send().getCells();
                results.addAll(cells);
                fromBlockNumber = currentToBlockNumber + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static String getAddress() {
        try {
            Script script = new Script(0, alwaysSuccessCellHash(), Collections.emptyList(), Collections.emptyList());
            return script.getTypeHash();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    private static OutPoint alwaysSuccessScriptOutPoint() throws IOException {
        String hash = genesisBlock().commitTransactions.get(0).hash;
        return new OutPoint(hash, 0);
    }

}
