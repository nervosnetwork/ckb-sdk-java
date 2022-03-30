package transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.transaction.LockUtils;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class LockUtilsTest {

  @Test
  public void testGenerateLockScriptWithPrivateKey() {
    Script lock =
        LockUtils.generateLockScriptWithPrivateKey(
            "e79f3207ea4980b7fed79956d5934249ceac4751a4fae01a0f7c4a96884bc4e3",
                Numeric.hexStringToByteArray("9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"));
    Assertions.assertArrayEquals(
            Numeric.hexStringToByteArray("9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
            lock.codeHash);
    Assertions.assertArrayEquals(
            Numeric.hexStringToByteArray("0x36c329ed630d6ce750712a477543672adab57f4c"),
            lock.args);
  }

  @Test
  public void testGenerateLockScriptWithAddress() {
    Script lock =
        LockUtils.generateLockScriptWithAddress(
            "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqdrhpvcu82numz73852ed45cdxn4kcn72cr4338a");
    Assertions.assertArrayEquals(
            Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"), lock.codeHash);
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("0xa3b8598e1d53e6c5e89e8acb6b4c34d3adb13f2b"), lock.args);
  }

  @Test
  public void testGenerateLockHashWithAddress() {
    byte[] lockHash =
        LockUtils.generateLockHashWithAddress("ckt1qyqrdsefa43s6m882pcj53m4gdnj4k440axqswmu83");

    Assertions.assertEquals(
        "0x1f2615a8dde4e28ca736ff763c2078aff990043f4cbf09eb4b3a58a140a0862d", Numeric.toHexString(lockHash));
  }
}
