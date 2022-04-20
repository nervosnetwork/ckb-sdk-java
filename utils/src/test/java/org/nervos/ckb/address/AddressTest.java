package org.nervos.ckb.address;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.exceptions.AddressFormatException;
import org.nervos.ckb.utils.Numeric;

public class AddressTest {

  @Test
  public void testPublicKeyHash() {
    String hash =
        Hash.blake160("0x24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
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
  public void testArgToAddressTestnet() throws AddressFormatException {
    String expected = "ckt1qyqz6824th6pekd6858nru9p4j3u783fttl4k3r0cp2lt7uxhx00fxcxpzeq8";
    byte[] args =
        Numeric.hexStringToByteArray(
            "0x2d1d555df41cd9ba3d0f31f0a1aca3cf1e295aff5b446fc055f5fb86b99ef49b");
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    String actual = utils.generate(args);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testArgToAddressTestnet1() throws AddressFormatException {
    String expected = "ckt1qypgzvf2uphwkpgykum7d0862wtmuddf9r0qnzefn9";
    byte[] args = Numeric.hexStringToByteArray("0x81312ae06eeb0504b737e6bcfa5397be35a928de");
    AddressUtils utils = new AddressUtils(Network.TESTNET, CodeHashType.ANYONE_CAN_APY);
    String actual = utils.generate(args);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testArgToAddressMainnet() throws AddressFormatException {
    String expected = "ckb1qypgzvf2uphwkpgykum7d0862wtmuddf9r0qw88kle";
    byte[] args = Numeric.hexStringToByteArray("0x81312ae06eeb0504b737e6bcfa5397be35a928de");
    AddressUtils utils = new AddressUtils(Network.MAINNET, CodeHashType.ANYONE_CAN_APY);
    String actual = utils.generate(args);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testArgToAddressMainnet1() throws AddressFormatException {
    String expected = "ckb1qyqrdsefa43s6m882pcj53m4gdnj4k440axqdt9rtd";
    byte[] args = Numeric.hexStringToByteArray("36c329ed630d6ce750712a477543672adab57f4c");
    AddressUtils utils = new AddressUtils(Network.MAINNET, CodeHashType.BLAKE160);
    String actual = utils.generate(args);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testPublicKeyHashToAddressTestnet() {
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    Assertions.assertEquals(
        "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83",
        utils.generateFromPublicKey(
            "0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"));
  }

  @Test
  public void testPublicKeyHashToAddressMainnet() {
    System.out.println(
        "0x24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01".length());
    AddressUtils utils = new AddressUtils(Network.MAINNET);
    Assertions.assertEquals(
        "ckb1qyqrdsefa43s6m882pcj53m4gdnj4k440axqdt9rtd",
        utils.generateFromPublicKey(
            "0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"));
  }

  @Test
  public void testPrivateKeyHashToAddressTestnet() {
    AddressUtils utils = new AddressUtils(Network.TESTNET);
    String privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
    String publicKey = ECKeyPair.publicKeyFromPrivate(privateKey);
    Assertions.assertEquals(
        "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83", utils.generateFromPublicKey(publicKey));
  }

  @Test
  public void testBlake160FromAddressTestnet1() {
    String blake160 = AddressUtils.parse("ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83");
    Assertions.assertEquals(blake160, "36c329ed630d6ce750712a477543672adab57f4c");
  }

  @Test
  public void testBlake160FromAddressTestnet2() {
    String blake160 = AddressUtils.parse("ckt1qyq2742g7yajdcahqsyn63spqrlspyy8g5pq7xg9tu");
    Assertions.assertEquals(blake160, "af5548f13b26e3b704093d460100ff0090874502");
  }

  @Test
  public void testBlake160FromAddressMainnet() {
    String blake160 = AddressUtils.parse("ckb1qyqrdsefa43s6m882pcj53m4gdnj4k440axqdt9rtd");
    Assertions.assertEquals(blake160, "36c329ed630d6ce750712a477543672adab57f4c");
  }

  @Test
  public void testPubkeyHashToAddressMainnetRFC() {
    AddressUtils utils = new AddressUtils(Network.MAINNET);
    Assertions.assertEquals(
        "ckb1qyqp8eqad7ffy42ezmchkjyz54rhcqf8q9pqrn323p",
        utils.generate(Numeric.hexStringToByteArray("0x13e41d6F9292555916f17B4882a5477C01270142")));
  }

  @Test
  public void testGenerateMultiAddress() {
    AddressUtils utils = new AddressUtils(Network.TESTNET, CodeHashType.MULTISIG);
    Assertions.assertEquals(
        "ckt1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lqh4kcuq",
        utils.generate(Numeric.hexStringToByteArray("f04cec84bc37f683613bed2f242c9aa1b678e9fe")));
  }

  @Test
  public void testParseMultiAddress() {
    Assertions.assertEquals(
        "f04cec84bc37f683613bed2f242c9aa1b678e9fe",
        AddressUtils.parse("ckt1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lqh4kcuq"));
  }
}
