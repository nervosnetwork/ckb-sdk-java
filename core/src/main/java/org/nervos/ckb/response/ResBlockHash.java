package org.nervos.ckb.response;

import org.nervos.ckb.response.Response;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResBlockHash extends Response<String> {

    public String getBlockHash() {
        return result;
    }

}
