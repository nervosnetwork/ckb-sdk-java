package sign;

import org.junit.jupiter.api.Test;

public class OmnilockSignerTest {

  @Test
  void testCkbSecp256k1Blake160SighashAll() {
    SignerChecker.signAndCheck("omnilock_secp256k1_blake160_sighash_all");
  }

  @Test
  void testCkbSecp256k1Blake160MultisigAll() {
    SignerChecker.signAndCheck("omnilock_secp256k1_blake160_multisig_all_first");
    SignerChecker.signAndCheck("omnilock_secp256k1_blake160_multisig_all_second");
  }
}
