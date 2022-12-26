package org.nervos.ckb.sign.omnilock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;

class OmnilockArgsTest {
  @Test
  public void testEncode() {
    OmnilockArgs.OmniConfig config = new OmnilockArgs.OmniConfig();
    config.setFlag(15);
    config.setAdminListCellTypeId(Numeric.hexStringToByteArray("0xeb760e1ad345001c6a31ae4ef531c38f94f64f2f6b6ac862a4822248adcb421c"));
    config.setMinimumCKBExponentInAcp(2);
    config.setMinimumSUDTExponentInAcp(1);
    config.setSinceForTimeLock(Long.parseUnsignedLong("129d5", 16));
    config.setTypeScriptHashForSupply(Numeric.hexStringToByteArray("0x1386a372b6b103f1de175dfb16c36f9358385d67239253131100fdd9624d699b"));
    byte[] expected = Numeric.hexStringToByteArray("0feb760e1ad345001c6a31ae4ef531c38f94f64f2f6b6ac862a4822248adcb421c020100000000000129d51386a372b6b103f1de175dfb16c36f9358385d67239253131100fdd9624d699b");
    Assertions.assertArrayEquals(expected, config.encode());
  }

  @Test
  public void testDecode() {
    byte[] bytes = Numeric.hexStringToByteArray("0feb760e1ad345001c6a31ae4ef531c38f94f64f2f6b6ac862a4822248adcb421c020100000000000129d51386a372b6b103f1de175dfb16c36f9358385d67239253131100fdd9624d699b");
    OmnilockArgs.OmniConfig config = OmnilockArgs.OmniConfig.decode(bytes);
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("0xeb760e1ad345001c6a31ae4ef531c38f94f64f2f6b6ac862a4822248adcb421c"), config.getAdminListCellTypeId());
    Assertions.assertEquals(2, config.getMinimumCKBExponentInAcp());
    Assertions.assertEquals(1, config.getMinimumSUDTExponentInAcp());
    Assertions.assertEquals(0x129d5, config.getSinceForTimeLock());
    Assertions.assertArrayEquals(Numeric.hexStringToByteArray("0x1386a372b6b103f1de175dfb16c36f9358385d67239253131100fdd9624d699b"), config.getTypeScriptHashForSupply());
  }
}
