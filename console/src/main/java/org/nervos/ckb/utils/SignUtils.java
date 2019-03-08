package org.nervos.ckb.utils;

import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.ECKeyPair;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.Sign;
import org.nervos.ckb.methods.type.CellInput;
import org.nervos.ckb.methods.type.CellOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class SignUtils {

    public static List<CellInput> signSigHashAllInputs(List<CellInput> cellInputs, List<CellOutput> cellOutputs, String privateKey) {
        String sigHashType = "1";
        Blake2b blake2b = Blake2b.getInstance();
        blake2b.update(sigHashType.getBytes());
        for (CellInput cellInput : cellInputs) {
            blake2b.update(Numeric.hexStringToByteArray(cellInput.previousOutput.hash));
            blake2b.update((String.valueOf(cellInput.previousOutput.index)).getBytes());
            blake2b.update(Numeric.hexStringToByteArray(cellInput.unlock.getTypeHash()));
        }

        for (CellOutput cellOutput : cellOutputs) {
            blake2b.update((String.valueOf(cellOutput.capacity)).getBytes());
            blake2b.update(Numeric.hexStringToByteArray(cellOutput.lock));
            if (cellOutput.type != null) {
                blake2b.update(Numeric.hexStringToByteArray(cellOutput.type.getTypeHash()));
            }
        }
        byte[] messageHash = blake2b.doFinalBytes();

        for (CellInput cellInput : cellInputs) {
            List<String> args = new ArrayList<>();
            args.add(signMessageForHexString(messageHash, privateKey));
            args.add(sigHashType);
            cellInput.unlock.args = args;
        }

        return cellInputs;

    }

    public static String signMessageForHexString(byte[] message, String privateKey) {
        ECKeyPair keyPair = ECKeyPair.createWithPrivateKey(Numeric.toBigInt(privateKey));
        byte[] signature = Sign.signMessage(message, keyPair).getDerSignature();
        return Numeric.toHexString(signature);
    }

}
