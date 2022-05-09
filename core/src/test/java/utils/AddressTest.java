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
  private Script script1 = new Script(
      Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
      Numeric.hexStringToByteArray("0xb39bbc0b3673c7d36450bc14cfcdad2d559c6c64"),
      Script.HashType.TYPE);
  private Script script2 = new Script(
      Numeric.hexStringToByteArray("0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8"),
      Numeric.hexStringToByteArray("0x4fb2be2e5d0c1a3b8694f832350a33c1685d477a"),
      Script.HashType.TYPE);
  private Script script3 = new Script(
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
    Address address = new Address(script1, Network.MAINNET);
    assertEquals("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v",
                 address.encodeShort());
    assertEquals("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks",
                 address.encodeFullBech32());
    assertEquals("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4",
                 address.encodeFullBech32m());

    address = new Address(script2, Network.MAINNET);
    assertEquals("ckb1qyq5lv479ewscx3ms620sv34pgeuz6zagaaqklhtgg",
                 address.encodeShort());

    address = new Address(script3, Network.MAINNET);
    assertEquals("ckb1qypt6p7e7v4uudxjw9f2dgper5ey77d2hp2qxz4u4u",
                 address.encodeShort());

    address = new Address(script4, Network.MAINNET);
    assertEquals("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq9nnw7qkdnnclfkg59uzn8umtfd2kwxceqvguktl",
                 address.encodeFullBech32m());
  }

  @Test
  public void testDecode() {
    Address expected = new Address(script1, Network.MAINNET);
    // short format
    Address actual = Address.decode("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v");
    assertEquals(expected, actual);
    // long bech32 format
    actual = Address.decode("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks");
    assertEquals(expected, actual);
    // long bech32m format
    actual = Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4");
    assertEquals(expected, actual);

    expected = new Address(script2, Network.MAINNET);
    actual = Address.decode("ckb1qyq5lv479ewscx3ms620sv34pgeuz6zagaaqklhtgg");
    assertEquals(expected, actual);

    expected = new Address(script3, Network.MAINNET);
    actual = Address.decode("ckb1qypt6p7e7v4uudxjw9f2dgper5ey77d2hp2qxz4u4u");
    assertEquals(expected, actual);

    expected = new Address(script4, Network.MAINNET);
    actual = Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq9nnw7qkdnnclfkg59uzn8umtfd2kwxceqvguktl");
    assertEquals(expected, actual);
  }
}
