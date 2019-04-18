package org.nervos.ckb.address;

import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

/**
 * Created by duanyytop on 2019-04-18.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 *
 * AddressUtils based on CKB AddressUtils Format [RFC](https://github.com/nervosnetwork/rfcs/blob/4f87099a0b1a02a8bc077fc7bea15ce3d9def120/rfcs/0000-address-format/0000-address-format.md),
 * and [Common AddressUtils Format](https://github.com/nervosnetwork/ckb/wiki/Common-Address-Format).
 */
public class AddressUtils {

    private static final String TYPE = "0x01";
    private static final String BIN_IDX = "P2PH";

    private Network network;

    public AddressUtils(Network network) {
        this.network = network;
    }

    public String generate(String publicKey) {
        // Payload: type(01) | bin-idx("P2PH") | pubkey blake160
        String payload = TYPE + strToAscii(BIN_IDX) + blake160(publicKey);
        return Bech32.encode(prefix(), convert(Numeric.hexStringToByteArray(payload), 8, 5, true));
    }

    public Bech32.Bech32Data parse(String address) {
        Bech32.Bech32Data parsed = Bech32.decode(address);
        byte[] data = convert(parsed.data, 5, 8, false);
        if (data.length == 0) {
            return null;
        }
        return new Bech32.Bech32Data(parsed.hrp, data);
    }

    private String prefix() {
        return network == Network.MAINNET? "ckb" : "ckt";
    }

    private String blake160(String value) {
        return Numeric.cleanHexPrefix(Hash.blake2b(value)).substring(0, 40);
    }

    private String strToAscii(String value) {
        StringBuilder sb = new StringBuilder();
        char[] letters = value.toCharArray();
        for (char ch : letters) {
            sb.append((byte) ch);
        }
        return sb.toString();
    }

    private static final byte[] EMPTY_ARRAY = {};
    private byte[] convert(byte[] data, int fromBits, int toBits, boolean pad) {
        StringBuilder ret = new StringBuilder();
        int acc = 0;
        int bits = 0;
        int maxv = (1 << toBits) - 1;
        for (int i = 0; i < data.length; i++) {
            int value = (int)data[i];
            if (value < 0 || (value >> fromBits != 0)) {
                return EMPTY_ARRAY;
            }
            acc = (acc << fromBits) | value;
            bits += fromBits;
            while (bits >= toBits) {
                bits -= toBits;
                ret.append((acc >> bits) & maxv);
            }
        }
        if (pad) {
            if (bits > 0) {
                ret.append((acc << (toBits - bits)) & maxv);
            }
        } else if (bits >= fromBits || ((acc << (toBits - bits)) & maxv) > 0) {
            return EMPTY_ARRAY;
        }
        return Numeric.hexStringToByteArray(ret.toString());
    }
}
