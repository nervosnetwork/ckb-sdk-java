package org.nervos.ckb.methods.type;

import org.nervos.ckb.crypto.Hash;
import java.util.List;

/**
 * Created by duanyytop on 2019-01-08.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Script {

    public int version;
    public String binary;
    public String reference;
    public List<String> signedArgs;
    public List<String> args;

    public Script(int version, String reference, List<String> signedArgs, List<String> args) {
        this.version = version;
        this.reference = reference;
        this.signedArgs = signedArgs;
        this.args = args;
    }

    public String getTypeHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(reference).append("|").append(binary);
        for (String str: signedArgs) {
            sb.append(str);
        }
        return Hash.sha3(sb.toString());
    }

}
