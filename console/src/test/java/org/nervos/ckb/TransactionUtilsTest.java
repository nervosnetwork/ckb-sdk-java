package org.nervos.ckb;

import org.junit.Assert;
import org.junit.Test;
import org.nervos.ckb.utils.TransactionUtils;

/**
 * Created by duanyytop on 2019-02-01.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class TransactionUtilsTest {

    @Test
    public void binToHexTest() {
        Assert.assertEquals("0x30646132666539396665353439653038326434656434383363326539363861383965613864313161616266356437396535636266303635323264653665363734",
                TransactionUtils.binToHex("0da2fe99fe549e082d4ed483c2e968a89ea8d11aabf5d79e5cbf06522de6e674"));
    }

}
