package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.utils.Numeric;

import java.util.Collections;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-08.
 * Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class Script {

    public static final String ALWAYS_SUCCESS_HASH = "0000000000000000000000000000000000000000000000000000000000000001";

    @JsonProperty("binary_hash")
    public String binaryHash;
    public List<String> args;

    public Script(){}

    public Script(String binaryHash, List<String> args) {
        this.binaryHash = binaryHash;
        this.args = args;
    }

    public static Script alwaysSuccess() {
        return new Script(ALWAYS_SUCCESS_HASH, Collections.emptyList());
    }

    public String getTypeHash() {
        Blake2b blake2b = new Blake2b();
        if (binaryHash != null) {
            blake2b.update(Numeric.hexStringToByteArray(binaryHash));
        }
        for (String arg: args) {
            blake2b.update(Numeric.hexStringToByteArray(arg));
        }
        return blake2b.doFinalString();
    }

}
