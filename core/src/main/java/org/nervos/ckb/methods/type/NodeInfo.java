package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by duanyytop on 2019-02-12.
 * Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class NodeInfo {


    /**
     * addresses : [{"address":"/ip4/0.0.0.0/tcp/12344/p2p/QmWRU2NSro4wKgVbFX6y8SPFkcJ1tE2X5xzk9msMhdRmdS","score":1}]
     * node_id : QmWRU2NSro4wKgVbFX6y8SPFkcJ1tE2X5xzk9msMhdRmdS
     * version : 0.5.0
     */

    @JsonProperty("node_id")
    public String nodeId;
    public String version;
    public List<Address> addresses;

    public static class Address {
        /**
         * address : /ip4/0.0.0.0/tcp/12344/p2p/QmWRU2NSro4wKgVbFX6y8SPFkcJ1tE2X5xzk9msMhdRmdS
         * score : 1
         */

        public String address;
        public int score;

    }
}
