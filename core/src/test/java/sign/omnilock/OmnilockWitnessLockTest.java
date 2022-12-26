package sign.omnilock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.omnilock.OmnilockWitnessLock;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.utils.Numeric;

class OmnilockWitnessLockTest {

  @Test
  public void testPackUnpack() {
    byte[] witness = Numeric.hexStringToByteArray("0x690000001000000069000000690000005500000055000000100000005500000055000000410000003434ca813dc378de0146aac8e60431fb52114acb3cb639f2fb2a479e1f219223532540413a154f440e939ee888c29221c0e8d6fef39402cbeedb6155317b356200");

    WitnessArgs witnessArgs = WitnessArgs.unpack(witness);
    OmnilockWitnessLock lock = OmnilockWitnessLock.unpack(witnessArgs.getLock());

    Assertions.assertEquals("0x55000000100000005500000055000000410000003434ca813dc378de0146aac8e60431fb52114acb3cb639f2fb2a479e1f219223532540413a154f440e939ee888c29221c0e8d6fef39402cbeedb6155317b356200",
                            Numeric.toHexString(lock.pack().toByteArray()));
  }
}
