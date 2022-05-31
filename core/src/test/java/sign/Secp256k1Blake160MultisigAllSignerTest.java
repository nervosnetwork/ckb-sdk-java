package sign;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.utils.Numeric;

import java.util.ArrayList;
import java.util.List;

public class Secp256k1Blake160MultisigAllSignerTest {
  @Test
  public void testMultiScriptEncode() {
    List<byte[]> keyHashes = new ArrayList<>();
    keyHashes.add(Numeric.hexStringToByteArray("0x9b41c025515b00c24e2e2042df7b221af5c1891f"));
    keyHashes.add(Numeric.hexStringToByteArray("0xe732dcd15b7618eb1d7a11e6a68e4579b5be0114"));
    Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript =
        new Secp256k1Blake160MultisigAllSigner.MultisigScript(0, 2, keyHashes);

    Assertions.assertArrayEquals(
        Numeric.hexStringToByteArray("0x000002029b41c025515b00c24e2e2042df7b221af5c1891fe732dcd15b7618eb1d7a11e6a68e4579b5be0114"),
        multisigScript.encode());
    Assertions.assertArrayEquals(
        Numeric.hexStringToByteArray("0x35ed7b939b4ac9cb447b82340fd8f26d344f7a62"),
        multisigScript.getArgs());
  }

  @Test
  public void testFirstSign() {
    SignerChecker.signAndCheck("secp256k1_blake16_multisig_all_first");
  }

  @Test
  public void testSecondSign() {
    SignerChecker.signAndCheck("secp256k1_blake16_multisig_all_second");
  }

}
