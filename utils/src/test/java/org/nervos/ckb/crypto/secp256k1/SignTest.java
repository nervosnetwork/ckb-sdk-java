package org.nervos.ckb.crypto.secp256k1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.nervos.ckb.utils.Numeric;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignTest {

  private ECKeyPair ecKeyPair;
  private byte[] message;

  @BeforeAll
  public void init() {
    String privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
    ecKeyPair = ECKeyPair.create(privateKey);
    message =
        Numeric.hexStringToByteArray(
            "403676bd85716a1e16b415093cee88c07d7cf2c2199aaf82320d354cb571f0d9");
  }

  @Test
  public void compressedPublicKeyTest() {
    String privateKey = "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3";
    String publicKey =
        Numeric.toHexStringNoPrefix(ECKeyPair.create(privateKey)
                                        .getEncodedPublicKey(true));
    Assertions.assertEquals(
        "024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01", publicKey);
  }

  @Test
  public void signMessageTest() {
    String signResult =
        "0xc795b2b3c48d370324e5053f4509d4f1f18f80aec4a8cba68ebae922b9f882d845ae312bd84e25eed818ef84e7ed61a774f208fe2b2fe3588b60b4686086208200";
    String signature = Numeric.toHexString(Sign.signMessage(message, ecKeyPair).getSignature());
    Assertions.assertEquals(signResult, signature);
  }

  @Test
  public void signMessageForDerFormatTest() {
    String signResult =
        "0x3045022100c795b2b3c48d370324e5053f4509d4f1f18f80aec4a8cba68ebae922b9f882d8022045ae312bd84e25eed818ef84e7ed61a774f208fe2b2fe3588b60b46860862082";
    String signature = Numeric.toHexString(Sign.signMessage(message, ecKeyPair).getDerSignature());
    Assertions.assertEquals(signResult, signature);
  }
}
