package org.nervos.ckb.methods.response.item;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public List<Cell.OutPoint> deps;
    public List<Input> inputs;
    public List<Output> outputs;

    public Transaction(){

    }

    public Transaction(int version, List<Cell.OutPoint> deps, List<Input> inputs, List<Output> outputs) {
        this.version = version;
        this.deps = deps;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public static class Input {

        /**
         * previous_output : {"hash":"0x0000000000000000000000000000000000000000000000000000000000000000","index":4294967295}
         * unlock : {"args":[],"binary":[1,0,0,0,0,0,0,0],"reference":null,"signed_args":[],"version":0}
         */

        @JsonProperty("previous_output")
        private PreviousOutput previousOutput;
        private Unlock unlock;

        public static class PreviousOutput {
            /**
             * hash : 0x0000000000000000000000000000000000000000000000000000000000000000
             * index : 4294967295
             */

            public String hash;
            public long index;

        }

        public static class Unlock {
            /**
             * args : []
             * binary: 0x0100000000000000
             * reference : null
             * signed_args : []
             * version : 0
             */

            public Object reference;
            public int version;
            public Byte[] args;
            public String binary;

            @JsonProperty("signed_args")
            public Byte[] signedArgs;

        }
    }

    public static class Output {

        /**
         * capacity : 50000
         * type : null
         * data : 0x
         * lock : 0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff
         */

        public long capacity;
        public Object type;
        public String lock;
        public String data;

    }

}
