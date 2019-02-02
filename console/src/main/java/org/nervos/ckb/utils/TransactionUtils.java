package org.nervos.ckb.utils;

import org.nervos.ckb.methods.type.CellInput;
import org.nervos.ckb.methods.type.CellOutput;
import org.nervos.ckb.methods.type.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-31.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class TransactionUtils {

    public static Transaction formatTx(Transaction transaction) {
        for (CellInput cellInput : transaction.inputs) {
            cellInput.unlock.signedArgs = formatList(cellInput.unlock.signedArgs);
            cellInput.unlock.args = formatList(cellInput.unlock.args);
            cellInput.unlock.binary = formatNonNullString(cellInput.unlock.binary);
        }
        for (CellOutput cellOutput : transaction.outputs) {
            cellOutput.data = binToHex(cellOutput.data);
            if (cellOutput.type != null) {
                cellOutput.type.signedArgs = formatList(cellOutput.type.signedArgs);
                cellOutput.type.args = formatList(cellOutput.type.args);
                cellOutput.type.binary = formatNonNullString(cellOutput.type.binary);
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
