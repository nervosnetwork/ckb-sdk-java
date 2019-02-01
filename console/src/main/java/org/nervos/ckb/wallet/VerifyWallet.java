package org.nervos.ckb.wallet;

import org.nervos.ckb.crypto.Sign;
import org.nervos.ckb.exception.CapacityException;
import org.nervos.ckb.methods.type.*;
import org.nervos.ckb.rpc.RpcRequest;
import org.nervos.ckb.utils.FileUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.SignUtils;
import org.nervos.ckb.utils.TransactionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class VerifyWallet extends BaseWallet {

    private static String MRUBY_CELL_HASH = "";
    private static String MRUBY_OUT_POINT_HASH = "";

    private String privateKey;

    public static VerifyWallet createWithPrivateKey(String privateKey) {
        VerifyWallet verifyWallet = new VerifyWallet();
        verifyWallet.privateKey = privateKey;
        return verifyWallet;
    }

    private VerifyWallet() {
    }

    public static void setMRubyCellHash(String mRubyCellHash) {
        MRUBY_CELL_HASH = mRubyCellHash;
    }

    public static void setMRubyOutPointHash(String mRubyOutPointHash) {
        MRUBY_OUT_POINT_HASH = mRubyOutPointHash;
    }

    @Override
    public long getBalance() {
        List<Cell> cells = getUnSpendCells(getAddress());
        long balance = 0;
        for (Cell cell: cells) {
            balance += cell.capacity;
        }
        return balance;
    }

    @Override
    public String getAddress() {
        return getUnlockScript().getTypeHash();
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
            ValidInputs validInputs = gatherInputs(getAddress(), capacity, Constant.MIN_CELL_CAPACITY);
            long inputCapacity = validInputs.capacity;
            if (inputCapacity < capacity) {
                throw new CapacityException("Not enough capacity paid");
            }
            List<Output> outputs = new ArrayList<>();
            outputs.add(new Output(capacity, "", toAddress));
            outputs.add(new Output(inputCapacity - capacity, "", getAddress()));
            return new Transaction(
                    0,
                    getDepsForOutPoint(),
                    SignUtils.signSigHashAllInputs(validInputs.inputs, outputs, privateKey),
                    outputs
            );
        } catch (CapacityException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected Script getUnlockScript() {
        String verifyScript = FileUtils.readFile("console/src/main/resources/bitcoin_unlock.rb");
        List<String> signedArgs = new ArrayList<>();
        signedArgs.add(verifyScript);
        signedArgs.add(String.format("%066x", Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey), true)));
        return new Script(0, MRUBY_CELL_HASH, signedArgs, Collections.emptyList());
    }

    @Override
    protected List<OutPoint> getDepsForOutPoint() {
        return Collections.singletonList(new OutPoint(MRUBY_OUT_POINT_HASH, 0));
    }


}
