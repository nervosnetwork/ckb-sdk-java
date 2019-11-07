package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.address.AddressGenerator;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AddressGeneratorTest {

  @Test
  void testSingleSigShortTestnetAddressGenerate() {
    Script singleSigShortScript =
        new Script(
            "9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.TESTNET, singleSigShortScript);
    Assertions.assertEquals("ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83", address);
  }

  @Test
  void testSingleSigShortMainnetAddressGenerate() {
    Script singleSigShortScript =
        new Script(
            "9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.MAINNET, singleSigShortScript);
    Assertions.assertEquals("ckb1qyqrdsefa43s6m882pcj53m4gdnj4k440axqdt9rtd", address);
  }

  @Test
  void testMultiSigShortTestnetAddressGenerate() {
    Script multiSigShortScript =
        new Script(
            "5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8",
            "f04cec84bc37f683613bed2f242c9aa1b678e9fe",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.TESTNET, multiSigShortScript);
    Assertions.assertEquals("ckt1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lqh4kcuq", address);
  }

  @Test
  void testMultiSigShortMainnetAddressGenerate() {
    Script multiSigShortScript =
        new Script(
            "5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8",
            "f04cec84bc37f683613bed2f242c9aa1b678e9fe",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.MAINNET, multiSigShortScript);
    Assertions.assertEquals("ckb1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lq2sg8su", address);
  }

  @Test
  void testTypeFullTestnetAddressGenerate() {
    Script typeFullScript =
        new Script(
            "1892ea40d82b53c678ff88312450bbb17e164d7a3e0a90941aa58839f56f8df2",
            "36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    String address = AddressGenerator.generateFullAddress(Network.TESTNET, typeFullScript);
    Assertions.assertEquals(
        "ckt1qsvf96jqmq4483ncl7yrzfzshwchu9jd0glq4yy5r2jcsw04d7xlydkr98kkxrtvuag8z2j8w4pkw2k6k4l5c02auef",
        address);
  }

  @Test
  void testTypeFullMainnetAddressGenerate() {
    Script typeFullScript =
        new Script(
            "1892ea40d82b53c678ff88312450bbb17e164d7a3e0a90941aa58839f56f8df2",
            "36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    String address = AddressGenerator.generateFullAddress(Network.MAINNET, typeFullScript);
    Assertions.assertEquals(
        "ckb1qsvf96jqmq4483ncl7yrzfzshwchu9jd0glq4yy5r2jcsw04d7xlydkr98kkxrtvuag8z2j8w4pkw2k6k4l5czfy37k",
        address);
  }

  @Test
  void testDataFullTestnetAddressGenerate() {
    Script dataFullScript =
        new Script(
            "a656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA);
    String address = AddressGenerator.generateFullAddress(Network.TESTNET, dataFullScript);
    Assertions.assertEquals(
        "ckt1q2n9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvdkr98kkxrtvuag8z2j8w4pkw2k6k4l5czshhac",
        address);
  }

  @Test
  void testDataFullMainnetAddressGenerate() {
    Script dataFullScript =
        new Script(
            "a656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA);
    String address = AddressGenerator.generateFullAddress(Network.MAINNET, dataFullScript);
    Assertions.assertEquals(
        "ckb1q2n9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvdkr98kkxrtvuag8z2j8w4pkw2k6k4l5c0nw668",
        address);
  }
}
