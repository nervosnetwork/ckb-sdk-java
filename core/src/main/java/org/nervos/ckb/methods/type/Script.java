package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.nervos.ckb.utils.Numeric;

import java.util.List;

/**
 * Created by duanyytop on 2019-01-08.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Script {

    public int version;
    public String binary;
    public String reference;
    @JsonProperty("signed_args")
    public List<String> signedArgs;
    public List<String> args;

    public Script(){}

    public Script(int version, String reference, List<String> signedArgs, List<String> args) {
        this.version = version;
        this.reference = reference;
        this.signedArgs = signedArgs;
        this.args = args;
    }

    public String getTypeHash() {
        SHA3.DigestSHA3 sha3 = new SHA3.Digest256();
        sha3.update(Numeric.hexStringToByteArray(reference));
        sha3.update("|".getBytes());
        if (binary != null) {
            sha3.update(Numeric.hexStringToByteArray(binary));
        }
        for (String arg: signedArgs) {
            sha3.update(arg.getBytes());
        }
        return Numeric.toHexString(sha3.digest());
    }

}
