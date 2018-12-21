package org.nervos.ckb.response;

import org.nervos.ckb.utils.Numeric;

import java.math.BigInteger;

/**
 * Created by duanyytop on 2018-12-21.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class ResBlockNumber extends Response<String> {

    public BigInteger getBlockNumber() {
        return Numeric.decodeQuantity(result);
    }

}
