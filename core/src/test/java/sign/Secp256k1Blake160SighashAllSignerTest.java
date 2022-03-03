package sign;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.signer.Secp256k1Blake160SighashAllSigner;

public class Secp256k1Blake160SighashAllSignerTest {

  @Test
  void testIsMatched() {
    Secp256k1Blake160SighashAllSigner signer = Secp256k1Blake160SighashAllSigner.getInstance();

    assertTrue(
        signer.isMatched(
            "9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f",
            "0xaf0b41c627807fbddcee75afa174d5a7e5135ebd"));
    assertFalse(
        signer.isMatched(
            "0x9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f",
            "0x0450340178ae277261a838c89f9ccb76a190ed4b"));
    assertFalse(signer.isMatched(null, "0xaf0b41c627807fbddcee75afa174d5a7e5135ebd"));
    assertFalse(
        signer.isMatched("9d8ca87d75d150692211fa62b0d30de4d1ee6c530d5678b40b8cedacf0750d0f", null));
  }

  @Test
  void testSecp256k1Blake160OneInput() {
    SignerChecker.signAndCheck("secp256k1_blake16_sighash_all_one_input");
  }

  @Test
  void testSecp256k1Blake160OneGroup() {
    SignerChecker.signAndCheck("secp256k1_blake16_sighash_all_one_group");
  }

  @Test
  void testSecp256k1Blake160TwoGroups() {
    SignerChecker.signAndCheck("secp256k1_blake16_sighash_all_two_groups");
  }
}
