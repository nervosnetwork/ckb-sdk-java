package org.nervos.ckb.wallet;

import org.nervos.ckb.exception.CapacityException;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.rpc.RpcRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanyytop on 2019-02-01.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public abstract class BaseWallet implements WalletAction {

    @Override
    public long getBalance() {
        List<Cell> cells = getUnSpendCells(getAddress());
        long balance = 0;
        for (Cell cell: cells) {
            balance += cell.capacity;
        }
        return balance;
    }

    protected abstract Transaction generateTx(String toAddress, long capacity);

    ValidInputs gatherInputs(String address, long capacity, long minCapacity) throws CapacityException {
        if (capacity < minCapacity) {
            throw new CapacityException("capacity cannot be less than " + minCapacity);
        }
        long inputCapacities = 0;
        List<Input> inputs = new ArrayList<>();
        List<Cell> cells = getUnSpendCells(address);
        for (Cell cell: cells) {
            Input input = new Input(new Input.PreviousOutput(cell.outPoint.hash, cell.outPoint.index), getUnlockScript());
            inputs.add(input);
            inputCapacities += cell.capacity;
            if (inputCapacities >= capacity && (inputCapacities - capacity) >= minCapacity) {
                break;
            }
        }
        if (inputCapacities < capacity) {
            throw new CapacityException("Not enough capacity!");
        }
        return new ValidInputs(inputs, inputCapacities);
    }

    class ValidInputs {
        List<Input> inputs;
        long capacity;

        ValidInputs(List<Input> inputs, long capacity) {
            this.inputs = inputs;
            this.capacity = capacity;
        }
    }

    protected abstract Script getUnlockScript();


    List<Cell> getUnSpendCells(String address) {
        List<Cell> results = new ArrayList<>();
        try {
            long toBlockNumber = RpcRequest.getTipBlockNumber().longValue();
            long fromBlockNumber = 1;
            while (fromBlockNumber <= toBlockNumber) {
                long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
                List<Cell> cells = RpcRequest.getCellsByTypeHash(address, fromBlockNumber, currentToBlockNumber);
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

    protected abstract List<OutPoint> getDepsForOutPoint();

}
