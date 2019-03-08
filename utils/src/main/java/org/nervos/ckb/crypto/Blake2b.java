package org.nervos.ckb.crypto;

import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.nervos.ckb.utils.Numeric;

/**
 * Created by duanyytop on 2019-03-08.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Blake2b {

    private Blake2bDigest blake2bDigest;

    public static Blake2b getInstance() {
        return Blake2bHolder.instance;
    }

    private static class Blake2bHolder {
        private static final Blake2b instance = new Blake2b();
    }

    private Blake2b() {
        blake2bDigest = new Blake2bDigest(null, 32, null, Hash.CKB_HASH_PERSONALIZATION);
    }

    public void update(byte[] input) {
        if (blake2bDigest != null) {
            blake2bDigest.update(input, 0, input.length);
        }
    }

    public byte[] doFinalBytes() {
        byte[] out = new byte[32];
        if (blake2bDigest != null) {
            blake2bDigest.doFinal(out, 0);
        }
        return out;
    }

    public String doFinalString() {
        return Numeric.toHexString(doFinalBytes());
    }
}
