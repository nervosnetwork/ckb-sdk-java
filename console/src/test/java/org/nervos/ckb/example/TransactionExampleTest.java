package org.nervos.ckb.example;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nervos.ckb.crypto.Hash;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.methods.type.Script;
import org.nervos.ckb.utils.Numeric;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionExampleTest {

  private TransactionExample transactionManager;
  private String privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e4";
  private Script inputLockScript;
  private String receiverAddress = "ckt1q9gry5zgaqtljzuuptt987pncdu9txh5570myf9amk0q3v";
  private String codeHash = "0x9e3b3557f11b2b3532ce352bfe8017e9fd11d154c4c7f9b7aaaa1e621b539a08";

  @BeforeAll
  public void init() {
    String publicKey = Sign.publicKeyFromPrivate(Numeric.toBigInt(privateKey), true).toString(16);
    String blake160 =
        Numeric.prependHexPrefix(Numeric.cleanHexPrefix(Hash.blake2b(publicKey)).substring(0, 40));
    inputLockScript = new Script(codeHash, Arrays.asList(blake160));
    transactionManager = new TransactionExample(privateKey, inputLockScript);
  }

  @Test
  public void testSendCapacity() throws Exception {
    TransactionExample.Receiver receiver =
        new TransactionExample.Receiver(receiverAddress, "6000000000");
    String hash = transactionManager.sendCapacity(Arrays.asList(receiver));
    Assertions.assertNotNull(hash);
  }
}
