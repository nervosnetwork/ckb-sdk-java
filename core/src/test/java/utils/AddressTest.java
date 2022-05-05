package utils;

import org.junit.jupiter.api.Test;
import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressTest {
  private Script script = new Script(
      Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
      Numeric.hexStringToByteArray("0xb39bbc0b3673c7d36450bc14cfcdad2d559c6c64"),
      Script.HashType.TYPE);

  @Test
  @SuppressWarnings("deprecation")
  public void testEncode() {
    Address address = new Address(script, Network.MAINNET);
    assertEquals("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v",
                 address.encodeShort());
    assertEquals("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks",
                 address.encodeFullBech32());
    assertEquals("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4",
                 address.encodeFullBech32m());
  }

  @Test
  public void testDecode() {
    Address expected = new Address(script, Network.MAINNET);
    // short format
    Address actual = Address.decode("ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v");
    assertEquals(expected, actual);
    // long bech32 format
    actual = Address.decode("ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks");
    assertEquals(expected, actual);
    // long bech32m format
    actual = Address.decode("ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4");
    assertEquals(expected, actual);
  }
}
