package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.exceptions.AddressFormatException;
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
  void testACPShortTestnetAddressParse() {
    String address = "ckt1qypgzvf2uphwkpgykum7d0862wtmuddf9r0qnzefn9";
    Script multiSigShortScript =
        new Script(
            "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
            "0x81312ae06eeb0504b737e6bcfa5397be35a928de",
            Script.TYPE);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.TESTNET, addressParseResult.network);
    Assertions.assertEquals(multiSigShortScript.args, addressParseResult.script.args);
    Assertions.assertEquals(multiSigShortScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(multiSigShortScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testACPShortMainnetAddressParse() {
    String address = "ckb1qypgzvf2uphwkpgykum7d0862wtmuddf9r0qw88kle";
    Script multiSigShortScript =
        new Script(
            "0xd369597ff47f29fbc0d47d2e3775370d1250b85140c670e4718af712983a2354",
            "0x81312ae06eeb0504b737e6bcfa5397be35a928de",
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

  @Test
  void testTypeFullMainnetAddressParseWithBech32m() {
    String address =
        "ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4";
    Script fullScript =
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0xb39bbc0b3673c7d36450bc14cfcdad2d559c6c64",
            Script.TYPE);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.MAINNET, addressParseResult.network);
    Assertions.assertEquals(fullScript.args, addressParseResult.script.args);
    Assertions.assertEquals(fullScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(fullScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testDataFullMainnetAddressParseWithBech32m() {
    String address =
        "ckb1qzn9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvqpkcv576ccddnn4quf2ga65xee2m26h7nqdcg257";
    Script fullScript =
        new Script(
            "0xa656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.MAINNET, addressParseResult.network);
    Assertions.assertEquals(fullScript.args, addressParseResult.script.args);
    Assertions.assertEquals(fullScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(fullScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testData1FullMainnetAddressParseWithBech32m() {
    String address =
        "ckb1qzn9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvqpkcv576ccddnn4quf2ga65xee2m26h7nqdcg257";
    Script fullScript =
        new Script(
            "0xa656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA);
    AddressParseResult addressParseResult = AddressParser.parse(address);
    Assertions.assertEquals(Network.MAINNET, addressParseResult.network);
    Assertions.assertEquals(fullScript.args, addressParseResult.script.args);
    Assertions.assertEquals(fullScript.codeHash, addressParseResult.script.codeHash);
    Assertions.assertEquals(fullScript.hashType, addressParseResult.script.hashType);
  }

  @Test
  void testParseAddressNetworkException() {
    String address = "ckn1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83";
    AddressFormatException exception =
        Assertions.assertThrows(
            AddressFormatException.class,
            new Executable() {
              @Override
              public void execute() throws Throwable {
                AddressParser.parseNetwork(address);
              }
            });
    Assertions.assertTrue(exception.getMessage().contains("Address prefix should be ckb or ckt"));
  }

  @Test
  void testFullAddressTypeException() {
    String address =
        "ckt1qwn9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvdkr98kkxrtvuag8z2j8w4pkw2k6k4l5ctv25r2";
    AddressFormatException exception =
        Assertions.assertThrows(
            AddressFormatException.class,
            new Executable() {
              @Override
              public void execute() throws Throwable {
                AddressParser.parse(address);
              }
            });
    Assertions.assertTrue(exception.getMessage().contains("Full address type must be 02 or 04"));
  }

  @Test
  void testShortAddressTypeException() {
    String address = "ckt1qyzndsefa43s6m882pcj53m4gdnj4k440axqcth0hp";
    AddressFormatException exception =
        Assertions.assertThrows(
            AddressFormatException.class,
            new Executable() {
              @Override
              public void execute() throws Throwable {
                AddressParser.parse(address);
              }
            });
    Assertions.assertTrue(
        exception.getMessage().contains("Short address code hash index must be 00, 01 or 02"));
  }

  @Test
  void testShortAddressArgsLengthException() {
    String address = "ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqqm65l9j";
    AddressFormatException exception =
        Assertions.assertThrows(
            AddressFormatException.class,
            new Executable() {
              @Override
              public void execute() throws Throwable {
                AddressParser.parse(address);
              }
            });
    Assertions.assertTrue(
        exception.getMessage().contains("Short address args byte length must be equal to 20"));
  }

  @Test
  void testAddressPayloadLengthException() {
    String address = "ckt1qsvf96jqmq4483ncl7yrzfzshwchu9jd0glq4yy5r2jcsw04r0l5xl";
    AddressFormatException exception =
        Assertions.assertThrows(
            AddressFormatException.class,
            new Executable() {
              @Override
              public void execute() throws Throwable {
                AddressParser.parse(address);
              }
            });
    Assertions.assertTrue(exception.getMessage().contains("Invalid full address payload length"));
  }

  @Test
  void testInvalidAddressParsing() {
    // These invalid addresses come form https://github.com/nervosnetwork/ckb-sdk-rust/pull/7/files
    // INVALID bech32 encoding
    Assertions.assertThrows(AddressFormatException.class, () -> AddressParser.parse("ckb1qyqylv479ewscx3ms620sv34pgeuz6zagaaqh0knz7"));
    // INVALID data length
    Assertions.assertThrows(AddressFormatException.class, () -> AddressParser.parse("ckb1qyqylv479ewscx3ms620sv34pgeuz6zagaarxdzvx03"));
    // INVALID code hash index
    Assertions.assertThrows(AddressFormatException.class, () -> AddressParser.parse("ckb1qyg5lv479ewscx3ms620sv34pgeuz6zagaaqajch0c"));
    // INVALID bech32m encoding
    Assertions.assertThrows(AddressFormatException.class, () -> AddressParser.parse("ckb1q2da0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsnajhch96rq68wrqn2tmhm"));
    // Invalid ckb2021 format full address
    Assertions.assertThrows(AddressFormatException.class, () -> AddressParser.parse("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq20k2lzuhgvrgacv4tmr88"));
    Assertions.assertThrows(AddressFormatException.class, () -> AddressParser.parse("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqz0k2lzuhgvrgacvhcym08"));
    Assertions.assertThrows(AddressFormatException.class, () -> AddressParser.parse("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqj0k2lzuhgvrgacvnhnzl8"));
  }
}
