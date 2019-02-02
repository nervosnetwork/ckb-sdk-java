package org.nervos.ckb.utils;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.nervos.ckb.crypto.ECKeyPair;
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
        SHA3.Digest256 sha3 = new SHA3.Digest256();
        sha3.update(sigHashType.getBytes());
        for (CellInput cellInput : cellInputs) {
            sha3.update(Numeric.hexStringToByteArray(cellInput.previousOutput.hash));
            sha3.update((String.valueOf(cellInput.previousOutput.index)).getBytes());
            sha3.update(Numeric.hexStringToByteArray(cellInput.unlock.getTypeHash()));
        }

        for (CellOutput cellOutput : cellOutputs) {
            sha3.update((String.valueOf(cellOutput.capacity)).getBytes());
            sha3.update(Numeric.hexStringToByteArray(cellOutput.lock));
            if (cellOutput.type != null) {
                sha3.update(Numeric.hexStringToByteArray(cellOutput.type.getTypeHash()));
            }
        }
        byte[] messageHash = sha3.digest();

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
