package org.nervos.ckb.response;

/**
 * Created by duanyytop on 2018-12-21.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResNodeId extends Response<String> {

    public String getNodeId() {
        return result;
    }

}
