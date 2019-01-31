package org.nervos.ckb.wallet;

import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.Sign;
import org.nervos.ckb.exception.CapacityException;
import org.nervos.ckb.exceptions.APIErrorException;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.service.CKBService;
import org.nervos.ckb.service.HttpService;
import org.nervos.ckb.utils.FileUtils;
import org.nervos.ckb.utils.Numeric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class WalletApi {

    private static final String MRUBY_CELL_HASH = "0x2165b10c4f6c55302158a17049b9dad4fef0acaf1065c63c02ddeccbce97ac47";
    private static final String MRUBY_OUT_POINT_HASH = "0x70b79cf5866b10c44ad175d24c101808d24729110fc48eb5ce562042f8526959";

    private CKBService ckbService;
    private String privateKey;
    private String address;

    public static WalletApi createWithPrivateKey(String privateKey) {
        WalletApi walletApi = new WalletApi();
        HttpService.setDebug(true);
        walletApi.ckbService = CKBService.build(new HttpService(Constant.NODE_URL));
        walletApi.privateKey = privateKey;
        walletApi.address = walletApi.verifyScript().getTypeHash();
        return walletApi;
    }

    public long getBalance() {
        List<Cell> cells = getUnspendCells();
        long balance = 0;
        for (Cell cell: cells) {
            balance += cell.capacity;
        }
        return balance;
    }

    public String getAddress() {
        return address;
    }

    public String sendCapacity(String toAddress, long capacity) throws IOException {
        Transaction tx = generateTx(toAddress, capacity);
        return ckbService.sendTransaction(Utils.formatTransaction(tx)).send().getTransactionHash();
    }

    public Transaction getTransactionByHash(String hash) throws IOException {
        return ckbService.getTransaction(hash).send().getTransaction();
    }

    public Transaction generateTx(String toAddress, long capacity) {
        try {
            ValidInput validInput = gatherInputs(capacity, Constant.MIN_CELL_CAPACITY);
            long inputCapacity = validInput.capacity;
            List<Output> outputs = new ArrayList<>();
            outputs.add(new Output(capacity, "", toAddress));
            if (inputCapacity > capacity) {
                outputs.add(new Output(inputCapacity - capacity, "", address));
            }
            return new Transaction(
                    0,
                    Arrays.asList(alwaysSuccessScriptOutPoint()),
                    Utils.signSigHashAllInputs(validInput.inputs, outputs, privateKey),
                    outputs
            );
        } catch (CapacityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ValidInput gatherInputs(long capacity, long minCapacity) throws CapacityException {
        if (capacity < minCapacity) {
            throw new CapacityException("capacity cannot be less than " + minCapacity);
        }
        long inputCapacities = 0;
        List<Input> inputs = new ArrayList<>();
        List<Cell> cells = getUnspendCells();
        for (Cell cell: cells) {
            Input input = new Input(new Input.PreviousOutput(cell.outPoint.hash, cell.outPoint.index), verifyScript());
            inputs.add(input);
            inputCapacities += cell.capacity;
            if (inputCapacities >= capacity && (inputCapacities - capacity) >= minCapacity) {
                break;
            }
        }
        if (inputCapacities < capacity) {
            throw new CapacityException("Not enough capacity!");
        }
        return new ValidInput(inputs, inputCapacities);
    }

    public class ValidInput {
        public List<Input> inputs;
        public long capacity;

        public ValidInput(List<Input> inputs, long capacity) {
            this.inputs = inputs;
            this.capacity = capacity;
        }
    }

    public Script verifyScript() {
        String verifyScript = FileUtils.readFile("console/src/main/resources/bitcoin_unlock.rb");
        List<String> signedArgs = new ArrayList<>();
        signedArgs.add(verifyScript);
        signedArgs.add(String.format("%066x", Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey), true)));
        return new Script(0, MRUBY_CELL_HASH, signedArgs);
    }


    public List<Cell> getUnspendCells() {
        List<Cell> results = new ArrayList<>();
        try {
            long toBlockNumber = ckbService.getTipBlockNumber().send().getBlockNumber().longValue();
            long fromBlockNumber = 1;
            while (fromBlockNumber <= toBlockNumber) {
                long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
                List<Cell> cells = ckbService.getCellsByTypeHash(address, fromBlockNumber, currentToBlockNumber).send().getCells();
                if (cells != null && cells.size() > 0) {
                    results.addAll(cells);
                }
                fromBlockNumber = currentToBlockNumber + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getMinerAddress() {
        try {
            Script script = new Script(0, alwaysSuccessCellHash(), Collections.emptyList());
            return script.getTypeHash();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Block genesisBlock() throws IOException {
        String blockHash = ckbService.getBlockHash(0).send().getBlockHash();
        return ckbService.getBlock(blockHash).send().getBlock();
    }


    private String alwaysSuccessCellHash() throws IOException {
        List<Output> systemCells = genesisBlock().commitTransactions.get(0).outputs;
        if (systemCells.isEmpty() || systemCells.get(0) == null) {
            throw new APIErrorException("Cannot find always success cell");
        }
        return Hash.sha3(systemCells.get(0).data);
    }

    private OutPoint alwaysSuccessScriptOutPoint() throws IOException {
//        String hash = genesisBlock().commitTransactions.get(0).hash;
        return new OutPoint(MRUBY_OUT_POINT_HASH, 0);
    }

}
