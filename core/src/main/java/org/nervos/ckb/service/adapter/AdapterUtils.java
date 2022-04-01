package org.nervos.ckb.service.adapter;

import org.bouncycastle.util.encoders.Hex;
import org.nervos.ckb.utils.Numeric;

import java.math.BigInteger;

public class AdapterUtils {
    protected static String toHexString(long src) {
        return toHexString(BigInteger.valueOf(src).toByteArray());
    }

    protected static String toHexString(byte[] src) {
        return removeLeadingZero(Numeric.toHexString(src));
    }

    protected static String toHexStringPadded(byte[] src) {
        String hex = Hex.toHexString(src);
        if (hex.length() % 2 == 1) {
            hex = "0" + hex;
        }
        return "0x" + hex;
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
