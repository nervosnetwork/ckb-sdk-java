package sign;

import org.junit.jupiter.api.Test;

public class OmnilockSignerTest {

  @Test
  void testCkbSecp256k1Blake160SighashAll() {
    SignerChecker.signAndCheck("omnilock_secp256k1_blake160_sighash_all");
  }
}
