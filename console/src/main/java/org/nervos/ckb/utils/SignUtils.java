package org.nervos.ckb.utils;

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
        Hash.update(sigHashType.getBytes());
        for (CellInput cellInput : cellInputs) {
            Hash.update(Numeric.hexStringToByteArray(cellInput.previousOutput.hash));
            Hash.update((String.valueOf(cellInput.previousOutput.index)).getBytes());
            Hash.update(Numeric.hexStringToByteArray(cellInput.unlock.getTypeHash()));
        }

        for (CellOutput cellOutput : cellOutputs) {
            Hash.update((String.valueOf(cellOutput.capacity)).getBytes());
            Hash.update(Numeric.hexStringToByteArray(cellOutput.lock));
            if (cellOutput.type != null) {
                Hash.update(Numeric.hexStringToByteArray(cellOutput.type.getTypeHash()));
            }
        }
        byte[] messageHash = Hash.doFinalBytes();

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
