package org.nervos.ckb.wallet;

import org.nervos.ckb.crypto.ECKeyPair;
import org.nervos.ckb.crypto.Keys;
import org.nervos.ckb.crypto.Sign;
import org.nervos.ckb.methods.type.Input;
import org.nervos.ckb.methods.type.Output;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.utils.HexUtil;
import org.nervos.ckb.utils.Numeric;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-29.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Utils {

    public static List<Input> signSigHashAllInputs(List<Input> inputs, List<Output> outputs, String privateKey) {
        String sigHashType = "0x1";
        StringBuilder hashBuilder = new StringBuilder(sigHashType);
        for (Input input: inputs) {
            hashBuilder.append(Numeric.cleanHexPrefix(input.previousOutput.hash));
            hashBuilder.append(input.previousOutput.index);
            hashBuilder.append(input.unlock.getTypeHash());
        }

        for (Output output: outputs) {
            hashBuilder.append(output.capacity);
            hashBuilder.append(Numeric.cleanHexPrefix(output.lock));
            if (output.type != null) {
                hashBuilder.append(output.type.getTypeHash());
            }
        }

        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        byte[] signature = Sign.signMessage(hashBuilder.toString().getBytes(), keyPair).getSignature();
        String signatureHex = HexUtil.bytesToHex(signature);

        for (Input input: inputs) {
            List<String> args = new ArrayList<>();
            args.add(signatureHex);
            args.add(sigHashType);
            input.unlock.args = args;
        }

        return inputs;

    }

}
