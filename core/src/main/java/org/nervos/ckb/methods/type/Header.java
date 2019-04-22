package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by duanyytop on 2018-12-21.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Header {

    /**
     * "difficulty": "0x100",
     * "hash": "0x087c25e23e42f5d1e00e6984241b3711742d5e0eaf75d79a427276473e1de3f9",
     * "number": 1,
     * "parent_hash": "0x9b0bd5be9498a0b873d08e242fff306eec04fac7c59ce479b49ca92a8f649982",
     * "seal": {
     *   "nonce": 16394887283531791882,
     *   "proof": "0xbd010000810200008a1300002e240000a9350000c4350000ea420000ca4d00005d5d0000766800004b6b000075730000"
     * },
     * "timestamp": 1545992487397,
     * "txs_commit": "0x3abd21e6e51674bb961bb4c5f3cee9faa5da30e64be10628dc1cef292cbae324",
     * "txs_proposal": "0x0000000000000000000000000000000000000000000000000000000000000000",
     * "uncles_count": 0,
     * "uncles_hash": "0x0000000000000000000000000000000000000000000000000000000000000000",
     * "version": 0
     */

    public String difficulty;
    public String hash;
    public int number;

    @JsonProperty("parent_hash")
    public String parentHash;

    public long timestamp;

    @JsonProperty("txs_commit")
    public String txsCommit;

    @JsonProperty("txs_proposal")
    public String txsProposal;

    @JsonProperty("witnesses_root")
    public String witnessesRoot;

    @JsonProperty("uncles_count")
    public int unclesCount;

    @JsonProperty("uncles_hash")
    public String unclesHash;

    public int version;

    public Seal seal;

    public static class Seal {
        /**
         * nonce : 16394887283531791882
         * proof : 0xbd010000810200008a1300002e240000a9350000c4350000ea420000ca4d00005d5d0000766800004b6b000075730000
         */

        public String nonce;
        public String proof;

    }
}
