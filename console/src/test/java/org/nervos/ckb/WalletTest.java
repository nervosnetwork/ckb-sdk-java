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
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";

  private static final String RECEIVE_ADDRESS = "ckt1qyqqtdpzfjwq7e667ktjwnv3hngrqkmwyhhqpa8dav";
  private static final String CODE_HASH =
      "0x54811ce986d5c3e57eaafab22cdd080e32209e39590e204a99b32935f835a13c";

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
    Wallet.Receiver receiver = new Wallet.Receiver(RECEIVE_ADDRESS, new BigInteger("10000000000"));
    String hash = transaction.sendCapacity(Collections.singletonList(receiver));
    Assertions.assertNotNull(hash);
  }
}
