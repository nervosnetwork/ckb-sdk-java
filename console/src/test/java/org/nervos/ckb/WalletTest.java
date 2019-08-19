package org.nervos.ckb;

import java.math.BigInteger;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WalletTest {

  private static final String PRIVATE_KEY =
      "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
  private static final String RECEIVE_ADDRESS = "ckt1qyqqtdpzfjwq7e667ktjwnv3hngrqkmwyhhqpa8dav";
  private static final String NODE_URL = "http://18.162.80.155:8144";

  @Test
  void testSendCapacity() throws Exception {
    Wallet wallet = new Wallet(PRIVATE_KEY, NODE_URL);
    Wallet.Receiver receiver = new Wallet.Receiver(RECEIVE_ADDRESS, new BigInteger("10000000000"));
    String hash = wallet.sendCapacity(Collections.singletonList(receiver));
    Assertions.assertNotNull(hash);
  }
}
