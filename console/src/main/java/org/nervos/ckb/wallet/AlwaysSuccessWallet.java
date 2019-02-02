package org.nervos.ckb.wallet;

import org.nervos.ckb.exception.CapacityException;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.rpc.RpcRequest;
import org.nervos.ckb.utils.TransactionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class AlwaysSuccessWallet extends BaseWallet {

    public AlwaysSuccessWallet(){

    }

    @Override
    public String sendCapacity(String toAddress, long capacity) {
        Transaction tx = generateTx(toAddress, capacity);
        try {
            return RpcRequest.sendTransaction(TransactionUtils.formatTx(tx));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Transaction generateTx(String toAddress, long capacity) {
        try {
            ValidInputs validInput = gatherInputs(getAddress(), capacity, Constant.MIN_CELL_CAPACITY);
            long inputCapacity = validInput.capacity;
            List<Output> outputs = new ArrayList<>();
            outputs.add(new Output(capacity, "", toAddress));
            if (inputCapacity > capacity) {
                outputs.add(new Output(inputCapacity - capacity, "", getAddress()));
            }
            return new Transaction(Constant.VERSION, getDepsForOutPoint(), validInput.inputs, outputs);
        } catch (CapacityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAddress() {
        return getUnlockScript().getTypeHash();
    }

    @Override
    protected Script getUnlockScript() {
        try {
            return new Script(Constant.VERSION, RpcRequest.alwaysSuccessCellHash(), Collections.emptyList(), Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected List<OutPoint> getDepsForOutPoint() {
        try {
            return RpcRequest.alwaysSuccessScriptOutPoint();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
