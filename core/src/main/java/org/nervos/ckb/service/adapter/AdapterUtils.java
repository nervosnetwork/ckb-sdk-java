package org.nervos.ckb.service.adapter;

import org.nervos.ckb.utils.Numeric;

public class AdapterUtils {
    protected static String toHexStringForNumber(byte[] src) {
        return onlyKeepSignificantHex(Numeric.toHexString(src));
    }

    public static String onlyKeepSignificantHex(String in) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        if (in.startsWith("0x")) {
            builder.append("0x");
            i = 2;
        }
        while (i < in.length() - 1 && in.charAt(i) == '0') {
            i++;
        }
        builder.append(in.substring(i));
        return builder.toString();
    }
}
