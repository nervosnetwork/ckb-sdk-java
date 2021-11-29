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
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.TESTNET, singleSigShortScript);
    Assertions.assertEquals(
        "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqfkcv576ccddnn4quf2ga65xee2m26h7nq4sds0r",
        address);
  }

  @Test
  void testSingleSigShortMainnetAddressGenerate() {
    Script singleSigShortScript =
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.MAINNET, singleSigShortScript);
    Assertions.assertEquals(
        "ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqfkcv576ccddnn4quf2ga65xee2m26h7nqmzxl9m",
        address);
  }

  @Test
  void testMultiSigShortTestnetAddressGenerate() {
    Script multiSigShortScript =
        new Script(
            "0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8",
            "0xf04cec84bc37f683613bed2f242c9aa1b678e9fe",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.TESTNET, multiSigShortScript);
    Assertions.assertEquals(
        "ckt1qpw9q60tppt7l3j7r09qcp7lxnp3vcanvgha8pmvsa3jplykxn32sq0sfnkgf0ph76pkzwld9ujzex4pkeuwnlsdc5tqu",
        address);
  }

  @Test
  void testMultiSigShortMainnetAddressGenerate() {
    Script multiSigShortScript =
        new Script(
            "0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8",
            "0xf04cec84bc37f683613bed2f242c9aa1b678e9fe",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.MAINNET, multiSigShortScript);
    Assertions.assertEquals(
        "ckb1qpw9q60tppt7l3j7r09qcp7lxnp3vcanvgha8pmvsa3jplykxn32sq0sfnkgf0ph76pkzwld9ujzex4pkeuwnlsr2ly2y",
        address);
  }

  @Test
  void testACPShortTestnetAddressGenerate() {
    Script acpShortScript =
        new Script(
            "0x3419a1c09eb2567f6552ee7a8ecffd64155cffe0f1796e6e61ec088d740c1356",
            "0x81312ae06eeb0504b737e6bcfa5397be35a928de",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.TESTNET, acpShortScript);
    Assertions.assertEquals(
        "ckt1qq6pngwqn6e9vlm92th84rk0l4jp2h8lurchjmnwv8kq3rt5psf4vqvpxy4wqmhtq5ztwdlxhna989a7xk5j3hsezjt38",
        address);
  }

  @Test
  void testACPShortMainnetAddressGenerate() {
    Script acpShortScript =
        new Script(
            "0xd369597ff47f29fbc0d47d2e3775370d1250b85140c670e4718af712983a2354",
            "0x81312ae06eeb0504b737e6bcfa5397be35a928de",
            Script.TYPE);
    String address = AddressGenerator.generate(Network.MAINNET, acpShortScript);
    Assertions.assertEquals(
        "ckb1qrfkjktl73ljn77q637judm4xux3y59c29qvvu8ywx90wy5c8g34gqvpxy4wqmhtq5ztwdlxhna989a7xk5j3hs5vltdv",
        address);
  }

  @Test
  void testTypeFullTestnetAddressGenerate() {
    Script typeFullScript =
        new Script(
            "0x1892ea40d82b53c678ff88312450bbb17e164d7a3e0a90941aa58839f56f8df2",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.TYPE);
    String address = AddressGenerator.generateFullAddress(Network.TESTNET, typeFullScript);
    Assertions.assertEquals(
        "ckt1qsvf96jqmq4483ncl7yrzfzshwchu9jd0glq4yy5r2jcsw04d7xlydkr98kkxrtvuag8z2j8w4pkw2k6k4l5c02auef",
        address);
  }

  @Test
  void testTypeFullMainnetAddressGenerateWithBech32m() {
    Script fullScript =
        new Script(
            "0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
            "0xb39bbc0b3673c7d36450bc14cfcdad2d559c6c64",
            Script.TYPE);
    String address = AddressGenerator.generateBech32mFullAddress(Network.MAINNET, fullScript);

    Assertions.assertEquals(
        "ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4",
        address);
  }

  @Test
  void testDataFullMainnetAddressGenerateWithBech32m() {
    Script fullScript =
        new Script(
            "0xa656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA);
    String address = AddressGenerator.generateBech32mFullAddress(Network.MAINNET, fullScript);

    Assertions.assertEquals(
        "ckb1qzn9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvqpkcv576ccddnn4quf2ga65xee2m26h7nqdcg257",
        address);
  }

  @Test
  void testData1FullMainnetAddressGenerateWithBech32m() {
    Script fullScript =
        new Script(
            "0xa656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA1);
    String address = AddressGenerator.generateBech32mFullAddress(Network.MAINNET, fullScript);

    Assertions.assertEquals(
        "ckb1qzn9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvq3kcv576ccddnn4quf2ga65xee2m26h7nqe5e7m2",
        address);
  }

  @Test
  void testTypeFullMainnetAddressGenerate() {
    Script typeFullScript =
        new Script(
            "0x1892ea40d82b53c678ff88312450bbb17e164d7a3e0a90941aa58839f56f8df2",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
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
            "0xa656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
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
            "0xa656f172b6b45c245307aeb5a7a37a176f002f6f22e92582c58bf7ba362e4176",
            "0x36c329ed630d6ce750712a477543672adab57f4c",
            Script.DATA);
    String address = AddressGenerator.generateFullAddress(Network.MAINNET, dataFullScript);
    Assertions.assertEquals(
        "ckb1q2n9dutjk669cfznq7httfar0gtk7qp0du3wjfvzck9l0w3k9eqhvdkr98kkxrtvuag8z2j8w4pkw2k6k4l5c0nw668",
        address);
  }
}
