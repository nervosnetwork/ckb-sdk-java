package org.nervos.ckb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.sign.SystemContract;
import org.nervos.ckb.type.Script;

class NetworkTest {
  @Test
  public void testGetSystemContract() {
    for (SystemContract.Type contractType: SystemContract.Type.values()) {
      Assertions.assertNotNull(Network.MAINNET.getSystemContract(contractType));
      Assertions.assertNotNull(Network.TESTNET.getSystemContract(contractType));
    }

    Assertions.assertNull(Network.MAINNET.getSystemContract((SystemContract.Type) null));
    Assertions.assertNull(Network.MAINNET.getSystemContract((Script) null));
  }
}