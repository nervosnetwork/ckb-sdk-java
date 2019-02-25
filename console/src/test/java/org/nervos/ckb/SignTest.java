package org.nervos.ckb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nervos.ckb.crypto.ECKeyPair;
import org.nervos.ckb.crypto.Sign;
import org.nervos.ckb.utils.Numeric;

/**
 * Created by duanyytop on 2019-02-01.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class SignTest {

    private ECKeyPair ecKeyPair;
    private byte[] message;

    @Before
    public void init() {
        String privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
        ecKeyPair = ECKeyPair.createWithPrivateKey(Numeric.toBigInt(privateKey));
        message = Numeric.hexStringToByteArray("85a1feafb48eae47b88308f525b759d651986e5a4d80a5915cb5d28566d6c7c5");
    }

    @Test
    public void signMessageTest() {
        String signResult = "0xbe85e76bf2c9ce4042dc9e1d12209ad552e826e83e1e4e8c06198a0fa28de17f5dd2b43723d7f819f26de60ef275d793229fd0b310c393d30584947f811ff37601";
        String signature = Numeric.toHexString(Sign.signMessage(message, ecKeyPair).getSignature());
        Assert.assertEquals(signResult, signature);
    }

    @Test
    public void signMessageForDerFormatTest() {
        String signResult = "0x3045022100be85e76bf2c9ce4042dc9e1d12209ad552e826e83e1e4e8c06198a0fa28de17f02205dd2b43723d7f819f26de60ef275d793229fd0b310c393d30584947f811ff376";
        String signature = Numeric.toHexString(Sign.signMessage(message, ecKeyPair).getDerSignature());
        Assert.assertEquals(signResult, signature);
    }

}
