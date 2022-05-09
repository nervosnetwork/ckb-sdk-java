package utils;

import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressTest {
  /**
   * Addresses for test come from https://github.com/rev-chaos/ckb-address-demo.
   */
  private Script secp256k1Blake160Script = new Script(
      Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
      Numeric.hexStringToByteArray("0xb39bbc0b3673c7d36450bc14cfcdad2d559c6c64"),
      Script.HashType.TYPE);
  private Script multiSignScript = new Script(
      Numeric.hexStringToByteArray("0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8"),
      Numeric.hexStringToByteArray("0x4fb2be2e5d0c1a3b8694f832350a33c1685d477a"),
      Script.HashType.TYPE);
  private Script acpScript = new Script(
      Numeric.hexStringToByteArray("0xd369597ff47f29fbc0d47d2e3775370d1250b85140c670e4718af712983a2354"),
      Numeric.hexStringToByteArray("bd07d9f32bce34d27152a6a0391d324f79aab854"),
      Script.HashType.TYPE);

  private Script script4 = new Script(
      Numeric.hexStringToByteArray("9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
      Numeric.hexStringToByteArray("b39bbc0b3673c7d36450bc14cfcdad2d559c6c64"),
      Script.HashType.DATA);

  @Test
  @SuppressWarnings("deprecation")
  public void testEncode() {
    Address address = new Address(secp256k1Blake160Script, Network.MAINNET);
    assertEquals("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v",
                 address.encodeShort());
    assertEquals("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks",
                 address.encodeFullBech32());
    assertEquals("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4",
                 address.encodeFullBech32m());

    assertEquals("ckb1qyq5lv479ewscx3ms620sv34pgeuz6zagaaqklhtgg",
                 new Address(multiSignScript, Network.MAINNET).encodeShort());
    assertEquals("ckt1qyq5lv479ewscx3ms620sv34pgeuz6zagaaqt6f5y5",
                 new Address(multiSignScript, Network.TESTNET).encodeShort());

    assertEquals("ckb1qypt6p7e7v4uudxjw9f2dgper5ey77d2hp2qxz4u4u",
                 new Address(acpScript, Network.MAINNET).encodeShort());

    assertEquals("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq9nnw7qkdnnclfkg59uzn8umtfd2kwxceqvguktl",
                 new Address(script4, Network.MAINNET).encodeFullBech32m());
    assertEquals("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq9nnw7qkdnnclfkg59uzn8umtfd2kwxceqz6hep8",
                 new Address(script4, Network.TESTNET).encodeFullBech32m());
  }

  @Test
  public void testDecode() {
    Address expected = new Address(secp256k1Blake160Script, Network.MAINNET);
    // short format
    Address actual = Address.decode("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v");
    assertEquals(expected, actual);
    // long bech32 format
    actual = Address.decode("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks");
    assertEquals(expected, actual);
    // long bech32m format
    actual = Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4");
    assertEquals(expected, actual);

    actual = Address.decode("ckb1qyq5lv479ewscx3ms620sv34pgeuz6zagaaqklhtgg");
    assertEquals(new Address(multiSignScript, Network.MAINNET), actual);
    actual = Address.decode("ckt1qyq5lv479ewscx3ms620sv34pgeuz6zagaaqt6f5y5");
    assertEquals(new Address(multiSignScript, Network.TESTNET), actual);

    actual = Address.decode("ckb1qypt6p7e7v4uudxjw9f2dgper5ey77d2hp2qxz4u4u");
    assertEquals(new Address(acpScript, Network.MAINNET), actual);

    actual = Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq9nnw7qkdnnclfkg59uzn8umtfd2kwxceqvguktl");
    assertEquals(new Address(script4, Network.MAINNET), actual);
    actual = Address.decode("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq9nnw7qkdnnclfkg59uzn8umtfd2kwxceqz6hep8");
    assertEquals(new Address(script4, Network.TESTNET), actual);
  }
}
