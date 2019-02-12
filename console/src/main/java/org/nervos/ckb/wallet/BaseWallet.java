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
        List<CellOutputWithOutPoint> cellOutputWithOutPoints = getUnSpendCells(getAddress());
        long balance = 0;
        for (CellOutputWithOutPoint cellOutputWithOutPoint : cellOutputWithOutPoints) {
            balance += cellOutputWithOutPoint.capacity;
        }
        return balance;
    }

    protected abstract Transaction generateTx(String toAddress, long capacity);

    protected abstract Script getUnlockScript();

    protected abstract List<OutPoint> getDepsForOutPoint();

    ValidInputs gatherInputs(String address, long capacity, long minCapacity) throws CapacityException {
        if (capacity < minCapacity) {
            throw new CapacityException("capacity cannot be less than " + minCapacity);
        }
        long inputCapacities = 0;
        List<CellInput> cellInputs = new ArrayList<>();
        List<CellOutputWithOutPoint> cellOutputs = getUnSpendCells(address);
        for (CellOutputWithOutPoint cellOutput : cellOutputs) {
            CellInput cellInput = new CellInput(new CellInput.PreviousOutput(cellOutput.outPoint.hash, cellOutput.outPoint.index), getUnlockScript());
            cellInputs.add(cellInput);
            inputCapacities += cellOutput.capacity;
            if (inputCapacities >= capacity && (inputCapacities - capacity) >= minCapacity) {
                break;
            }
        }
        if (inputCapacities < capacity) {
            throw new CapacityException("Not enough capacity!");
        }
        return new ValidInputs(cellInputs, inputCapacities);
    }

    class ValidInputs {
        final List<CellInput> cellInputs;
        final long capacity;

        ValidInputs(List<CellInput> cellInputs, long capacity) {
            this.cellInputs = cellInputs;
            this.capacity = capacity;
        }
    }

    List<CellOutputWithOutPoint> getUnSpendCells(String address) {
        List<CellOutputWithOutPoint> results = new ArrayList<>();
        try {
            long toBlockNumber = RpcRequest.getTipBlockNumber().longValue();
            long fromBlockNumber = 1;
            while (fromBlockNumber <= toBlockNumber) {
                long currentToBlockNumber = Math.min(fromBlockNumber + 100, toBlockNumber);
                List<CellOutputWithOutPoint> cellOutputs = RpcRequest.getCellsByTypeHash(address, fromBlockNumber, currentToBlockNumber);
                if (cellOutputs != null && cellOutputs.size() > 0) {
                    results.addAll(cellOutputs);
                }
                fromBlockNumber = currentToBlockNumber + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

}
