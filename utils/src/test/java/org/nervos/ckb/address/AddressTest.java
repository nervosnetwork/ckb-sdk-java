package org.nervos.ckb.address;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.utils.Bech32;
import org.nervos.ckb.utils.Network;
import org.nervos.ckb.utils.Numeric;

/** Created by duanyytop on 2019-04-18. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class AddressTest {

  @Test
  public void testPublicKeyHash() {
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    String hash =
        utils.blake160("0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
    Assertions.assertEquals("36c329ed630d6ce750712a477543672adab57f4c", hash);
  }

  @Test
  public void testAddressAscii() {
    String bin = "P2PH";
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    String data = utils.strToAscii(bin);
    Assertions.assertEquals("50325048", data);
  }

  @Test
  public void testAddressParse() {
    String address = "ckt1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6v234ygf";
    String payload = "0x015032504836c329ed630d6ce750712a477543672adab57f4c";
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    Bech32.Bech32Data bech32Data = utils.parse(address);
    Assertions.assertEquals(payload, Numeric.toHexString(bech32Data.data));
  }

  @Test
  public void testArgToAddressTestnet() throws AddressFormatException {
    String expected = "ckt1q9gry5zg95w42h05rnvm50g0x8c2rt9reu0zjkhltdzxlsz47hacdwv77jds9waehx";
    String arg = "0x2d1d555df41cd9ba3d0f31f0a1aca3cf1e295aff5b446fc055f5fb86b99ef49b";
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    String actual = utils.generateFromArg(arg);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testPublicKeyHashToAddressTestnet() {
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    Assertions.assertEquals(
        "ckt1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6v234ygf",
        utils.generate("0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"));
  }

  @Test
  public void testPublicKeyHashToAddressMainnet() {
    AddressUtils utils = new AddressUtils(Network.MAINNET);
    Assertions.assertEquals(
        "ckb1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6vqdd7em",
        utils.generate("0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"));
  }

  @Test
  public void testPrivateKeyHashToAddressTestnet() {
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    String publicKey =
        Sign.publicKeyFromPrivate(
                Numeric.toBigInt(
                    "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"),
                true)
            .toString(16);
    Assertions.assertEquals(
        "ckt1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6v234ygf", utils.generate(publicKey));
  }

  @Test
  public void testBlake160FromAddressTestnet() {
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    String blake160 =
        utils.getBlake160FromAddress("ckt1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6v234ygf");
    Assertions.assertEquals(blake160, "0x36c329ed630d6ce750712a477543672adab57f4c");
  }

  @Test
  public void testBlake160FromAddressMainnet() {
    AddressUtils utils = new AddressUtils(Network.MAINNET);
    String blake160 =
        utils.getBlake160FromAddress("ckb1q9gry5zgxmpjnmtrp4kww5r39frh2sm89tdt2l6vqdd7em");
    Assertions.assertEquals(blake160, "0x36c329ed630d6ce750712a477543672adab57f4c");
  }
}
