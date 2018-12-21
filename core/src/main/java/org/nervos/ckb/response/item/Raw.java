package org.nervos.ckb.response.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Raw {

    /**
     * cellbase_id : 0xbddb7c2559c2c3cdfc8f3cae2697ca75489521c352265cc9e60b4b2416ad5929
     * difficulty : 0x100
     * number : 1
     * parent_hash : 0x9b0bd5be9498a0b873d08e242fff306eec04fac7c59ce479b49ca92a8f649982
     * timestamp : 1544599720510
     * txs_commit : 0xbddb7c2559c2c3cdfc8f3cae2697ca75489521c352265cc9e60b4b2416ad5929
     * txs_proposal : 0x0000000000000000000000000000000000000000000000000000000000000000
     * uncles_count : 0
     * uncles_hash : 0x0000000000000000000000000000000000000000000000000000000000000000
     * version : 0
     */

    @SerializedName("cellbase_id")
    public String cellbaseId;

    public String difficulty;
    public int number;

    @SerializedName("parent_hash")
    public String parentHash;

    public long timestamp;

    @SerializedName("txs_commit")
    public String txsCommit;

    @SerializedName("txs_proposal")
    public String txsProposal;

    @SerializedName("uncles_count")
    public int unclesCount;

    @SerializedName("uncles_hash")
    public String unclesHash;

    public int version;

}
