package util;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Witness;

public class WitnessTest {
  @Test
  public void testGenerateWitness() {
    List<String> witness =
        Witness.generateWitness(
            Numeric.toBigInt("e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3"),
            "0xe4253550e49e1a78c8ae9e9e7f66506e11ce87d102fd1aad5dad04c88eca2bb2");
    Assertions.assertEquals(
        witness.get(0), "0x024a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01");
    Assertions.assertEquals(
        witness.get(1),
        "0x304402207d29d0b659bc6c7cd6545938b93e11910abfa1864c52588934876dfa3dd3bb570220067e61b89989525c3551530a4c17bb5e76c7723ab6690bd5c492bd39cb4b35f9");
    Assertions.assertEquals(witness.get(2), "0x4600000000000000");
  }
}
