package org.nervos.ckb.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.ckb.utils.Numeric;

import java.nio.charset.StandardCharsets;

public class Blake2bTest {

  @Test
  public void testBlake2b() {
    Blake2b blake2b = new Blake2b();
    blake2b.update("The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8));

    Assertions.assertArrayEquals(
        Numeric.hexStringToByteArray("0xabfa2c08d62f6f567d088d6ba41d3bbbb9a45c241a8e3789ef39700060b5cee2"),
        blake2b.doFinalBytes());
  }
}
