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
            input.unlock.signedArgs = formatList(input.unlock.signedArgs);
            input.unlock.args = formatList(input.unlock.args);
            input.unlock.binary = formatNonNullString(input.unlock.binary);
        }
        for (Output output: transaction.outputs) {
            output.data = binToHex(output.data);
            if (output.type != null) {
                output.type.signedArgs = formatList(output.type.signedArgs);
                output.type.args = formatList(output.type.args);
                output.type.binary = formatNonNullString(output.type.binary);
            }
        }
        return transaction;
    }

    private static String formatNonNullString(String value) {
        return Strings.isEmpty(value)? value : binToHex(value);
    }

    private static List<String> formatList(List<String> values) {
        List<String> results = new ArrayList<>();
        for (String arg: values) {
            results.add(binToHex(arg));
        }
        return results;
    }


    public static String binToHex(String value) {
        return Numeric.toHexString(value.getBytes());
    }

}
