package org.nervos.ckb.crypto;

import org.junit.Assert;
import org.junit.Test;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Numeric;

/**
 * Created by duanyytop on 2019-04-18.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class Bech32Test {

    @Test
    public void testEncode() {
        byte[] data  = new byte[]{0, 14, 20, 15, 7, 13, 26, 0, 25, 18, 6, 11, 13, 8, 21, 4, 20, 3, 17, 2, 29, 3, 12, 29, 3, 4, 15, 24, 20, 6, 14, 30, 22};
        Assert.assertEquals("bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4", Bech32.encode("bc", data));
    }

    @Test
    public void testDecode() {
        byte[] data  = new byte[]{0, 14, 20, 15, 7, 13, 26, 0, 25, 18, 6, 11, 13, 8, 21, 4, 20, 3, 17, 2, 29, 3, 12, 29, 3, 4, 15, 24, 20, 6, 14, 30, 22};
        Bech32.Bech32Data bech32Data = Bech32.decode("bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4");
        Assert.assertEquals(Numeric.toHexString(data), Numeric.toHexString(bech32Data.data));
    }


}
