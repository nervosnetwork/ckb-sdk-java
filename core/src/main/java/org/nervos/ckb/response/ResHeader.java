package org.nervos.ckb.response;

import org.nervos.ckb.response.item.Header;

/**
 * Created by duanyytop on 2018-12-21.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResHeader extends Response<Header> {

    public Header getHeader() {
        return result;
    }

}
