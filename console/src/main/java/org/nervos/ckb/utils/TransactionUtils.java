package org.nervos.ckb.utils;

import org.nervos.ckb.methods.type.Input;
import org.nervos.ckb.methods.type.Output;
import org.nervos.ckb.methods.type.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-31.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class TransactionUtils {

    public static Transaction formatTx(Transaction transaction) {
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
