package org.nervos.ckb;

import java.math.BigInteger;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.utils.Numeric;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WalletTest {

  private Wallet transaction;
  private static final String PRIVATE_KEY =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e4";

  private static final String RECEIVE_ADDRESS =
      "ckt1q9gry5zgaqtljzuuptt987pncdu9txh5570myf9amk0q3v";
  private static final String CODE_HASH =
      "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08";

  @BeforeAll
  public void init() {
    String publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(PRIVATE_KEY), true).toString(16);
    String blake160 =
        Numeric.prependHexPrefix(Numeric.cleanHexPrefix(Hash.blake2b(publicKey)).substring(0, 40));
    Script inputLockScript = new Script(CODE_HASH, Collections.singletonList(blake160));
    transaction = new Wallet(PRIVATE_KEY, inputLockScript, "http://localhost:8114");
  }

  @Test
  public void testSendCapacity() throws Exception {
    Wallet.Receiver receiver = new Wallet.Receiver(RECEIVE_ADDRESS, new BigInteger("6000000000"));
    String hash = transaction.sendCapacity(Collections.singletonList(receiver));
    Assertions.assertNotNull(hash);
  }
}
