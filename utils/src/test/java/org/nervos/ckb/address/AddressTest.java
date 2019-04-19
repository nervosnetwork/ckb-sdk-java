package org.nervos.ckb.address;

import org.junit.Assert;
import org.junit.Test;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

/**
 * Created by duanyytop on 2019-04-18.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class AddressTest {

    @Test
    public void testPublicKeyHash() {
        AddressUtils utils = new AddressUtils(Network.TESTNET);
        String hash = utils.blake160("0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
        Assert.assertEquals("36c329ed630d6ce750712a477543672adab57f4c", hash);
    }

    @Test
    public void testConvertBits() {
        byte[] from = { (byte) 0b11001101, (byte) 0b11011101 };
        byte[] expectedTo = { 0b00011001, 0b00010111, 0b00001110, 0b00010000 };
        AddressUtils utils = new AddressUtils(Network.TESTNET);
        byte[] convertBits = utils.convertBits(from, 8, 5, false);
        Assert.assertArrayEquals(expectedTo, convertBits);
    }

    @Test
    public void testAddressAscii() {
        String bin = "P2PH";
        AddressUtils utils = new AddressUtils(Network.TESTNET);
        String data = utils.strToAscii(bin);
        Assert.assertEquals("50325048", data);
    }

    @Test
    public void testAddressParse() {
        String address = "ckt1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6v234ygf";
        AddressUtils utils = new AddressUtils(Network.TESTNET);
        Bech32.Bech32Data bech32Data = utils.parse(address);
        Assert.assertEquals(address, Numeric.toHexString(bech32Data.data));
    }

    @Test
    public void testPublicKeyHashToAddressTestnet() {
        AddressUtils utils = new AddressUtils(Network.TESTNET);
        Assert.assertEquals("ckt1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6v234ygf",
                utils.generate("0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"));
    }


    @Test
    public void testPublicKeyHashToAddressMainnet() {
        AddressUtils utils = new AddressUtils(Network.MAINNET);
        Assert.assertEquals("ckb1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6vqdd7em",
                utils.generate("0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"));
    }

}
