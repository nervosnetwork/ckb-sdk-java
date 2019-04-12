package org.nervos.ckb.methods.type;

import java.util.List;

/**
 * Created by duanyytop on 2018-12-21.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Transaction {

    /**
     * deps : []
     * inputs : [{"previous_output":{"hash":"0x0000000000000000000000000000000000000000000000000000000000000000","index":4294967295},"unlock":{"args":[],"binary":[1,0,0,0,0,0,0,0],"reference":null,"signed_args":[],"version":0}}]
     * outputs : [{"capacity":50000,"contract":null,"data":[],"lock":"0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff"}]
     * version : 0
     */

    public int version;
    public String hash;
    public List<OutPoint> deps;
    public List<CellInput> inputs;
    public List<CellOutput> outputs;
    public List<Witness> witnesses;

    public Transaction() {

    }

    public Transaction(int version, List<OutPoint> deps, List<CellInput> cellInputs, List<CellOutput> cellOutputs, List<Witness> witnesses) {
        this.version = version;
        this.deps = deps;
        this.inputs = cellInputs;
        this.outputs = cellOutputs;
        this.witnesses = witnesses;
    }


}
