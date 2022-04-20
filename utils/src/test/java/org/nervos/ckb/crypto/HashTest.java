package org.nervos.ckb.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.nervos.ckb.utils.Numeric.asByte;

public class HashTest {

  @Test
  public void testBlake2b() {
    Assertions.assertEquals(
        Hash.blake2b(""), "0x44f4c69744d5f8c55d642062949dcae49bc4e7ef43d388c5a12f42b5633d163e");

    Assertions.assertEquals(
        Hash.blake2bString("The quick brown fox jumps over the lazy dog"),
        "0xabfa2c08d62f6f567d088d6ba41d3bbbb9a45c241a8e3789ef39700060b5cee2");
  }

  @Test
  public void testBlake160() {
    Assertions.assertEquals(
        Hash.blake160("24a501efd328e062c8675f2365970728c859c592beeefd6be8ead3d901330bc01"),
        "36c329ed630d6ce750712a477543672adab57f4c");
  }

  @Test
  public void testByte() {
    Assertions.assertEquals(asByte(0x0, 0x0), (byte) 0x0);
    Assertions.assertEquals(asByte(0x1, 0x0), (byte) 0x10);
    Assertions.assertEquals(asByte(0xf, 0xf), (byte) 0xff);
    Assertions.assertEquals(asByte(0xc, 0x5), (byte) 0xc5);
  }
}
