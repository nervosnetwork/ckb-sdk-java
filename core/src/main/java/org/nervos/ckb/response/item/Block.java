package org.nervos.ckb.response.item;

import java.util.List;

/**
 * Created by duanyytop on 2018-12-20.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Block {

    /**
     * hash : 0x7643567cc0b8637505cce071ae764bc17a1d4e37579769c9a863d25841e48a07
     * header : {"raw":{"cellbase_id":"0xbddb7c2559c2c3cdfc8f3cae2697ca75489521c352265cc9e60b4b2416ad5929","difficulty":"0x100","number":1,"parent_hash":"0x9b0bd5be9498a0b873d08e242fff306eec04fac7c59ce479b49ca92a8f649982","timestamp":1544599720510,"txs_commit":"0xbddb7c2559c2c3cdfc8f3cae2697ca75489521c352265cc9e60b4b2416ad5929","txs_proposal":"0x0000000000000000000000000000000000000000000000000000000000000000","uncles_count":0,"uncles_hash":"0x0000000000000000000000000000000000000000000000000000000000000000","version":0},"seal":{"nonce":"10545468520399447721","proof":[163,13,0,0,12,17,0,0,98,28,0,0,240,60,0,0,200,62,0,0,12,76,0,0,6,93,0,0,247,93,0,0,107,97,0,0,230,100,0,0,16,103,0,0,244,107,0,0]}}
     * transactions : [{"hash":"0xbddb7c2559c2c3cdfc8f3cae2697ca75489521c352265cc9e60b4b2416ad5929","transaction":{"deps":[],"inputs":[{"previous_output":{"hash":"0x0000000000000000000000000000000000000000000000000000000000000000","index":4294967295},"unlock":{"args":[],"binary":[1,0,0,0,0,0,0,0],"reference":null,"signed_args":[],"version":0}}],"outputs":[{"capacity":50000,"contract":null,"data":[],"lock":"0x321c1ca2887fb8eddaaa7e917399f71e63e03a1c83ff75ed12099a01115ea2ff"}],"version":0}}]
     */

    public String hash;
    public Header header;
    public List<Transaction> transactions;

}
