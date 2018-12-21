package org.nervos.ckb.response.item;

import java.util.List;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Transaction {

    /**
     * hash : 0xbddb7c2559c2c3cdfc8f3cae2697ca75489521c352265cc9e60b4b2416ad5929
     * transaction : {"deps":[],"inputs":[{"previous_output":{"hash":"0x0000000000000000000000000000000000000000000000000000000000000000","index":4294967295},"unlock":{"args":[],"binary":[1,0,0,0,0,0,0,0],"reference":null,"signed_args":[],"version":0}}],"outputs":[{"capacity":50000,"contract":null,"data":[],"lock":"0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff"}],"version":0}
     */

    public String hash;
    public TransactionResult transaction;

    public static class TransactionResult {

        /**
         * deps : []
         * inputs : [{"previous_output":{"hash":"0x0000000000000000000000000000000000000000000000000000000000000000","index":4294967295},"unlock":{"args":[],"binary":[1,0,0,0,0,0,0,0],"reference":null,"signed_args":[],"version":0}}]
         * outputs : [{"capacity":50000,"contract":null,"data":[],"lock":"0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff"}]
         * version : 0
         */

        public int version;
        public List<?> deps;
        public List<Input> inputs;
        public List<Output> outputs;

    }
}
