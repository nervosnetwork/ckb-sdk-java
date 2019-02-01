package org.nervos.ckb.utils;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.nervos.ckb.crypto.ECKeyPair;
import org.nervos.ckb.crypto.Sign;
import org.nervos.ckb.methods.type.Input;
import org.nervos.ckb.methods.type.Output;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class SignUtils {

    public static List<Input> signSigHashAllInputs(List<Input> inputs, List<Output> outputs, String privateKey) {
        String sigHashType = "1";
        SHA3.Digest256 sha3 = new SHA3.Digest256();
        sha3.update(sigHashType.getBytes());
        for (Input input: inputs) {
            sha3.update(Numeric.hexStringToByteArray(input.previousOutput.hash));
            sha3.update((String.valueOf(input.previousOutput.index)).getBytes());
            sha3.update(Numeric.hexStringToByteArray(input.unlock.getTypeHash()));
        }

        for (Output output: outputs) {
            sha3.update((String.valueOf(output.capacity)).getBytes());
            sha3.update(Numeric.hexStringToByteArray(output.lock));
            if (output.type != null) {
                sha3.update(Numeric.hexStringToByteArray(output.type.getTypeHash()));
            }
        }
        byte[] messageHash = sha3.digest();

        for (Input input: inputs) {
            List<String> args = new ArrayList<>();
            args.add(signMessageForHexString(messageHash, privateKey));
            args.add(sigHashType);
            input.unlock.args = args;
        }

        return inputs;

    }

    public static String signMessageForHexString(byte[] message, String privateKey) {
        ECKeyPair keyPair = ECKeyPair.createWithPrivateKey(Numeric.toBigInt(privateKey));
        byte[] signature = Sign.signMessage(message, keyPair).getDerSignature();
        return Numeric.toHexString(signature);
    }

}
