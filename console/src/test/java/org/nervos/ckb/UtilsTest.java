package org.nervos.ckb;

import org.junit.Assert;
import org.junit.Test;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.SignUtils;
import org.nervos.ckb.utils.TransactionUtils;

/**
 * Created by duanyytop on 2019-02-01.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class UtilsTest {

    @Test
    public void binToHexTest() {
        Assert.assertEquals("0x30646132666539396665353439653038326434656434383363326539363861383965613864313161616266356437396535636266303635323264653665363734",
                TransactionUtils.binToHex("0da2fe99fe549e082d4ed483c2e968a89ea8d11aabf5d79e5cbf06522de6e674"));
    }

    @Test
    public void signMessageForHexStringTest() {
        byte[] message = Numeric.hexStringToByteArray("85a1feafb48eae47b88308f525b759d651986e5a4d80a5915cb5d28566d6c7c5");
        String privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
        String signature = SignUtils.signMessageForHexString(message, privateKey);
        Assert.assertEquals("0x3045022100be85e76bf2c9ce4042dc9e1d12209ad552e826e83e1e4e8c06198a0fa28de17f02205dd2b43723d7f819f26de60ef275d793229fd0b310c393d30584947f811ff376", signature);
    }

}
