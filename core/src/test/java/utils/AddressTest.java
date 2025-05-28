package utils;

import org.junit.jupiter.api.Test;
import org.nervos.ckb.Network;
import org.nervos.ckb.type.MultisigVersion;
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
  @Test
  public void testDecodeDecode() {
    // Short
    Script script = generateScript(Script.SECP256K1_BLAKE160_SIGNHASH_ALL_CODE_HASH,
                                   "b39bbc0b3673c7d36450bc14cfcdad2d559c6c64", Script.HashType.TYPE);
    testShort(script, Network.MAINNET, "ckb1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jqfwyw5v");
    testShort(script, Network.TESTNET, "ckt1qyqt8xaupvm8837nv3gtc9x0ekkj64vud3jq5t63cs");

    script = generateScript(Script.SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_LEGACY,
                            "4fb2be2e5d0c1a3b8694f832350a33c1685d477a", Script.HashType.TYPE);
    testShort(script, Network.MAINNET, "ckb1qyq5lv479ewscx3ms620sv34pgeuz6zagaaqklhtgg");
    testShort(script, Network.TESTNET, "ckt1qyq5lv479ewscx3ms620sv34pgeuz6zagaaqt6f5y5");

    script = generateScript(Script.ANY_CAN_PAY_CODE_HASH_MAINNET,
                            "bd07d9f32bce34d27152a6a0391d324f79aab854", Script.HashType.TYPE);
    testShort(script, Network.MAINNET, "ckb1qypt6p7e7v4uudxjw9f2dgper5ey77d2hp2qxz4u4u");
    script = generateScript(Script.ANY_CAN_PAY_CODE_HASH_TESTNET,
                            "bd07d9f32bce34d27152a6a0391d324f79aab854", Script.HashType.TYPE);
    testShort(script, Network.TESTNET, "ckt1qypt6p7e7v4uudxjw9f2dgper5ey77d2hp2qm8treq");

    // Full bech32
    script = generateScript("9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                            "b39bbc0b3673c7d36450bc14cfcdad2d559c6c64", Script.HashType.DATA);
    testFullBech32(script, Network.MAINNET, "ckb1q2da0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgdwd2q8");
    testFullBech32(script, Network.TESTNET, "ckt1q2da0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgqd588c");
    script = generateScript("9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                            "b39bbc0b3673c7d36450bc14cfcdad2d559c6c64", Script.HashType.TYPE);
    testFullBech32(script, Network.MAINNET, "ckb1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxgj53qks");
    testFullBech32(script, Network.TESTNET, "ckt1qjda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xw3vumhs9nvu786dj9p0q5elx66t24n3kxglhgd30");

    // Full bech32m
    script = generateScript("9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                            "b39bbc0b3673c7d36450bc14cfcdad2d559c6c64", Script.HashType.DATA);
    testFullBech32m(script, Network.MAINNET, "ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq9nnw7qkdnnclfkg59uzn8umtfd2kwxceqvguktl");
    testFullBech32m(script, Network.TESTNET, "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq9nnw7qkdnnclfkg59uzn8umtfd2kwxceqz6hep8");

    script = generateScript("9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                            "b39bbc0b3673c7d36450bc14cfcdad2d559c6c64", Script.HashType.TYPE);
    testFullBech32m(script, Network.MAINNET, "ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqxwquc4");
    testFullBech32m(script, Network.TESTNET, "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdnnw7qkdnnclfkg59uzn8umtfd2kwxceqgutnjd");

    script = generateScript("9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8",
                            "b39bbc0b3673c7d36450bc14cfcdad2d559c6c64", Script.HashType.DATA1);
    testFullBech32m(script, Network.MAINNET, "ckb1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq4nnw7qkdnnclfkg59uzn8umtfd2kwxceqcydzyt");
    testFullBech32m(script, Network.TESTNET, "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq4nnw7qkdnnclfkg59uzn8umtfd2kwxceqkkxdwn");

    // multiscript v2

    script = generateScript(MultisigVersion.V2.codeHash(),
                            "986b5c23988427044e57d188fee45530d8877bcc", MultisigVersion.V2.hashType());
    testFullBech32m(script, Network.MAINNET, "ckb1qqmvjudc6s0mm992hjnhm367sfnjntycg3a5d7g7qpukz4wamvxjjq5cddwz8xyyyuzyu4733rlwg4fsmzrhhnqvclulh");
    testFullBech32m(script, Network.TESTNET, "ckt1qqmvjudc6s0mm992hjnhm367sfnjntycg3a5d7g7qpukz4wamvxjjq5cddwz8xyyyuzyu4733rlwg4fsmzrhhnqz25n40");

    script = generateScript(MultisigVersion.Legacy.codeHash(),
                            "986b5c23988427044e57d188fee45530d8877bcc", MultisigVersion.Legacy.hashType());
    testFullBech32m(script, Network.MAINNET, "ckb1qpw9q60tppt7l3j7r09qcp7lxnp3vcanvgha8pmvsa3jplykxn32sqvcddwz8xyyyuzyu4733rlwg4fsmzrhhnqq5gm75");
    testFullBech32m(script, Network.TESTNET, "ckt1qpw9q60tppt7l3j7r09qcp7lxnp3vcanvgha8pmvsa3jplykxn32sqvcddwz8xyyyuzyu4733rlwg4fsmzrhhnqwxr55v");
  }

  private void testShort(Script script, Network network, String encoded) {
    Address address = new Address(script, network);
    assertEquals(encoded, address.encodeShort());
    assertEquals(address, Address.decode(encoded));
  }

  private void testFullBech32(Script script, Network network, String encoded) {
    Address address = new Address(script, network);
    assertEquals(encoded, address.encodeFullBech32());
    assertEquals(address, Address.decode(encoded));
  }

  private void testFullBech32m(Script script, Network network, String encoded) {
    Address address = new Address(script, network);
    assertEquals(encoded, address.encodeFullBech32m());
    assertEquals(address, Address.decode(encoded));
  }

  @Test
  public void testInvalidDecode() {
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

  private Script generateScript(String codeHash, String args, Script.HashType hashType) {
    return new Script(
        Numeric.hexStringToByteArray(codeHash),
        Numeric.hexStringToByteArray(args),
        hashType);
  }

  private Script generateScript(byte[] codeHash, String args, Script.HashType hashType) {
    return new Script(
        codeHash,
        Numeric.hexStringToByteArray(args),
        hashType);
  }
}
