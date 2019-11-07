package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.address.AddressParseResult;
import org.nervos.ckb.utils.address.AddressParser;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AddressParserTest {

  @Test
  void testSingleSigShortTestnetAddressParse() {
    String address = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";
    Script singleSigShortScript =
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.TESTNET, addressParseResult.network);
    Assertions.assertEquals(singleSigShortScript.args, addressParseResult.script.args);
    Assertions.assertEquals(singleSigShortScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(singleSigShortScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testSingleSigShortMainnetAddressParse() {
    String address = "ckb1qyqrdsefa43s6m882pcj53m4gdnj4k440axqdt9rtd";
    Script singleSigShortScript =
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.MAINNET, addressParseResult.network);
    Assertions.assertEquals(singleSigShortScript.args, addressParseResult.script.args);
    Assertions.assertEquals(singleSigShortScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(singleSigShortScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testMultiSigShortTestnetAddressParse() {
    String address = "ckt1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lqh4kcuq";
    Script multiSigShortScript =
        new Script(
            "0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8",
            "0xf04cec84bc37f683613bed2f242c9aa1b678e9fe",
            Script.TYPE);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.TESTNET, addressParseResult.network);
    Assertions.assertEquals(multiSigShortScript.args, addressParseResult.script.args);
    Assertions.assertEquals(multiSigShortScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(multiSigShortScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testMultiSigShortMainnetAddressParse() {
    String address = "ckb1qyqlqn8vsj7r0a5rvya76tey9jd2rdnca8lq2sg8su";
    Script multiSigShortScript =
        new Script(
            "0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8",
            "0xf04cec84bc37f683613bed2f242c9aa1b678e9fe",
            Script.TYPE);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.MAINNET, addressParseResult.network);
    Assertions.assertEquals(multiSigShortScript.args, addressParseResult.script.args);
    Assertions.assertEquals(multiSigShortScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(multiSigShortScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testTypeFullTestnetAddressParse() {
    String address =
        "ckt1qsvf96jqmq4483ncl7yrzfzshwchu9jd0glq4yy5r2jcsw04d7xlydkr98kkxrtvuag8z2j8w4pkw2k6k4l5c02auef";
    Script typeFullScript =
        new Script(
            "0x1892ea40d82b53c678ff88312450bbb17e164d7a3e0a90941aa58839f56f8df2",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.TESTNET, addressParseResult.network);
    Assertions.assertEquals(typeFullScript.args, addressParseResult.script.args);
    Assertions.assertEquals(typeFullScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(typeFullScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testTypeFullMainnetAddressParse() {
    String address =
        "ckb1qsvf96jqmq4483ncl7yrzfzshwchu9jd0glq4yy5r2jcsw04d7xlydkr98kkxrtvuag8z2j8w4pkw2k6k4l5czfy37k";
    Script typeFullScript =
        new Script(
            "0x1892ea40d82b53c678ff88312450bbb17e164d7a3e0a90941aa58839f56f8df2",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.MAINNET, addressParseResult.network);
    Assertions.assertEquals(typeFullScript.args, addressParseResult.script.args);
    Assertions.assertEquals(typeFullScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(typeFullScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testDataFullTestnetAddressParse() {
    String address =
        "ckt1q2n9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvdkr98kkxrtvuag8z2j8w4pkw2k6k4l5czshhac";
    Script dataFullScript =
        new Script(
            "0xa656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.TESTNET, addressParseResult.network);
    Assertions.assertEquals(dataFullScript.args, addressParseResult.script.args);
    Assertions.assertEquals(dataFullScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(dataFullScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testDataFullMainnetAddressParse() {
    String address =
        "ckb1q2n9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvdkr98kkxrtvuag8z2j8w4pkw2k6k4l5c0nw668";
    Script dataFullScript =
        new Script(
            "0xa656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.MAINNET, addressParseResult.network);
    Assertions.assertEquals(dataFullScript.args, addressParseResult.script.args);
    Assertions.assertEquals(dataFullScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(dataFullScript.hashType, addressParseResult.script.hashType);
  }
}
