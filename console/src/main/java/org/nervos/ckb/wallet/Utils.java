package org.nervos.ckb.wallet;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.nervos.ckb.crypto.ECKeyPair;
import org.nervos.ckb.crypto.Sign;
import org.nervos.ckb.methods.response.CkbBlock;
import org.nervos.ckb.methods.type.Input;
import org.nervos.ckb.methods.type.Output;
import org.nervos.ckb.methods.type.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Utils {

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

        ECKeyPair keyPair = ECKeyPair.createWithPrivateKey(Numeric.toBigInt(privateKey));
        byte[] signature = Sign.signMessage(messageHash, keyPair).getSignature();
        String signatureHex = Numeric.toHexString(signature);

        for (Input input: inputs) {
            List<String> args = new ArrayList<>();
            args.add(signatureHex);
            args.add(sigHashType);
            input.unlock.args = args;
        }

        return inputs;

    }


    public static Transaction formatTransaction(Transaction transaction) {
        for (Input input: transaction.inputs) {
            List<String> signedArgs = new ArrayList<>();
            for (String arg: input.unlock.signedArgs) {
                signedArgs.add(binToHex(arg));
            }
            input.unlock.signedArgs = signedArgs;

            List<String> args = new ArrayList<>();
            for (String arg: input.unlock.args) {
                args.add(binToHex(arg));
            }
            input.unlock.args = args;

            if (!Strings.isEmpty(input.unlock.binary)) {
                input.unlock.binary = binToHex(input.unlock.binary);
            }
        }
        for (Output output: transaction.outputs) {
            output.data = binToHex(output.data);
            if (output.type != null) {
                List<String> signedArgs = new ArrayList<>();
                for (String arg: output.type.signedArgs) {
                    signedArgs.add(binToHex(arg));
                }
                output.type.signedArgs = signedArgs;

                List<String> args = new ArrayList<>();
                for (String arg: output.type.args) {
                    args.add(binToHex(arg));
                }
                output.type.args = args;

                if (!Strings.isEmpty(output.type.binary)) {
                    output.type.binary = binToHex(output.type.binary);
                }
            }
        }
        return transaction;
    }


    private static String binToHex(String value) {
        return Numeric.toHexString(value.getBytes());
    }

}
