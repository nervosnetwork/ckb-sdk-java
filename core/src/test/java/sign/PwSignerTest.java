package sign;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.sign.signer.PwSigner;
import org.nervos.ckb.utils.Numeric;

public class PwSignerTest {

  @Test
  void testIsMatched() {
    PwSigner signer = PwSigner.getInstance();

    Assertions.assertTrue(
        signer.isMatched(
            ECKeyPair.create("f8f8a2f43c8376ccb0871305060d7b27b0554d2cc72bccf41b2705608452f315"),
            Numeric.hexStringToByteArray("001d3f1ef827552ae1114027bd3ecf1f086ba0f9")));
    Assertions.assertTrue(
        signer.isMatched(
            ECKeyPair.create("e0ccb2548af279947b452efda4535dd4bcadf756d919701fcd4c382833277f85"),
            Numeric.hexStringToByteArray("adabffb9c27cb4af100ce7bca6903315220e87a2")));
  }

  @Test
  void testPwOneGroup() {
    SignerChecker.signAndCheck("pw_one_group");
  }
}
