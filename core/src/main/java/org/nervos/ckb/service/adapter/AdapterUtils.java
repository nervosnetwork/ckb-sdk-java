package org.nervos.ckb.service.adapter;

import org.nervos.ckb.utils.Numeric;

import java.math.BigInteger;

public class AdapterUtils {
    protected static String toHexString(long src) {
        return toHexString(BigInteger.valueOf(src).toByteArray());
    }

    protected static String toHexString(byte[] src) {
        return removeLeadingZero(Numeric.toHexString(src));
    }

    private static String removeLeadingZero(String hex) {
        hex = Numeric.cleanHexPrefix(hex);
        int i = 0;
        while (i < hex.length() - 1 && hex.charAt(i) == '0') {
            i++;
        }
        if (i > 0) {
            hex = hex.substring(i);
        }
        return "0x" + hex;
    }
}
