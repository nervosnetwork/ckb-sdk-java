package org.nervos.ckb.response;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResTransactionHash extends Response<String> {

    public String getTransactionHash() {
        return result;
    }

}
