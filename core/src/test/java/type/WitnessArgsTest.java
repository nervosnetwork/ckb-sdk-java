package type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.utils.Numeric;

public class WitnessArgsTest {
  @Test
  public void testSerialization() {
    WitnessArgs witnessArgs = new WitnessArgs();
    witnessArgs.setLock(new byte[65]);
    byte[] argsBytes = Numeric.hexStringToByteArray("0x55000000100000005500000055000000410000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    Assertions.assertArrayEquals(argsBytes, witnessArgs.pack().toByteArray());
    Assertions.assertEquals(WitnessArgs.unpack(argsBytes), witnessArgs);

    witnessArgs.setInputType(new byte[]{(byte) 0xab});
    witnessArgs.setOutputType(new byte[]{(byte) 0xcd, (byte) 0xef});
    argsBytes = Numeric.hexStringToByteArray("0x6000000010000000550000005a00000041000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000ab02000000cdef");
    Assertions.assertArrayEquals(argsBytes, witnessArgs.pack().toByteArray());
  }
}