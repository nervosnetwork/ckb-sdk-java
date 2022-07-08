package utils;

import org.junit.jupiter.api.Test;
import org.nervos.ckb.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.ckb.utils.address.AddressFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  private Script scriptWithHashTypeData = new Script(
      Numeric.hexStringToByteArray("0x709f3fda12f561cfacf92273c57a98fede188a3f1a59b1f888d113f9cce08649"),
      Numeric.hexStringToByteArray("0xb73961e46d9eb118d3de1d1e8f30b3af7bbf3160"),
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

    assertEquals("ckb1qfcf7076zt6krnavly3883t6nrlduxy28ud9nv0c3rg387wvuzryndeev8jxm843rrfau8g73uct8tmmhuckqy57acj",
                 new Address(scriptWithHashTypeData, Network.MAINNET).encodeFullBech32());
    assertEquals("ckb1qpcf7076zt6krnavly3883t6nrlduxy28ud9nv0c3rg387wvuzryjq9h89s7gmv7kyvd8hsar68npva00wlnzcqgh76tz",
                 new Address(scriptWithHashTypeData, Network.MAINNET).encodeFullBech32m());
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

    actual = Address.decode("ckb1qfcf7076zt6krnavly3883t6nrlduxy28ud9nv0c3rg387wvuzryndeev8jxm843rrfau8g73uct8tmmhuckqy57acj");
    assertEquals(new Address(scriptWithHashTypeData, Network.MAINNET), actual);
    actual = Address.decode("ckb1qpcf7076zt6krnavly3883t6nrlduxy28ud9nv0c3rg387wvuzryjq9h89s7gmv7kyvd8hsar68npva00wlnzcqgh76tz");
    assertEquals(new Address(scriptWithHashTypeData, Network.MAINNET), actual);
  }


  @Test
  void testInvalidDecode() {
    // These invalid addresses come form https://github.com/nervosnetwork/ckb-sdk-rust/pull/7/files
    // INVALID bech32 encoding
    assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qyqylv479ewscx3ms620sv34pgeuz6zagaaqh0knz7"));
    // INVALID data length
    assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qyqylv479ewscx3ms620sv34pgeuz6zagaarxdzvx03"));
    // INVALID code hash index
    assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qyg5lv479ewscx3ms620sv34pgeuz6zagaaqajch0c"));
    // INVALID bech32m encoding
    assertThrows(AddressFormatException.class, () -> Address.decode("ckb1q2da0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsnajhch96rq68wrqn2tmhm"));
    // Invalid ckb2021 format full address
    assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq20k2lzuhgvrgacv4tmr88"));
    assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqz0k2lzuhgvrgacvhcym08"));
    assertThrows(AddressFormatException.class, () -> Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqj0k2lzuhgvrgacvnhnzl8"));
  }
}
