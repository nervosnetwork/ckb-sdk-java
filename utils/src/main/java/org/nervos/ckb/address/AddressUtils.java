package org.nervos.ckb.address;

import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.exceptions.AddressFormatException;
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

    private static final String TYPE = "01";
    private static final String BIN_IDX = "P2PH";

    private Network network;

    public AddressUtils(Network network) {
        this.network = network;
    }

    public String generate(String publicKey) {
        // Payload: type(01) | bin-idx("P2PH") | pubkey blake160
        String payload = TYPE + strToAscii(BIN_IDX) + blake160(publicKey);
        byte[] data = Numeric.hexStringToByteArray(payload);
        return Bech32.encode(prefix(), convertBits(data, 8, 5, true));
    }

    public Bech32.Bech32Data parse(String address) {
        Bech32.Bech32Data parsed = Bech32.decode(address);
        byte[] data = convertBits(parsed.data, 5, 8, false);
        if (data.length == 0) {
            return null;
        }
        return new Bech32.Bech32Data(parsed.hrp, data);
    }

    private String prefix() {
        return network == Network.MAINNET? "ckb" : "ckt";
    }

    public String blake160(String value) {
        return Numeric.cleanHexPrefix(Hash.blake2b(value)).substring(0, 40);
    }

    public String strToAscii(String value) {
        StringBuilder sb = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append(Integer.toHexString((int)chars[i]));
        }
        return sb.toString();
    }

    public byte[] convertBits(byte[] data, int fromBits, int toBits, boolean pad) {
        int length = (int) (pad ? Math.floor((double) data.length * fromBits / toBits)
                : Math.ceil((double) data.length * fromBits / toBits));
        int mask = ((1 << toBits) - 1) & 0xff;
        byte[] result = new byte[length];
        int index = 0;
        int accumulator = 0;
        int bits = 0;
        for (int i = 0; i < data.length; i++) {
            byte value = data[i];
            accumulator = (((accumulator & 0xff) << fromBits) | (value & 0xff));
            bits += fromBits;
            while (bits >= toBits) {
                bits -= toBits;
                result[index] = (byte) ((accumulator >> bits) & mask);
                ++index;
            }
        }
        if (!pad) {
            if (bits > 0) {
                result[index] = (byte) ((accumulator << (toBits - bits)) & mask);
            }
        } else {
            if (!(bits < fromBits && ((accumulator << (toBits - bits)) & mask) == 0)) {
                throw new AddressFormatException("Strict mode was used but input couldn't be converted without padding");
            }
        }
        return result;
    }
}
