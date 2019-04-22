package org.nervos.ckb.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/** Created by duanyytop on 2019-03-08. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Blake2bTest {

  @Test
  public void testBlake2bUpdate() {
    Blake2b blake2b = new Blake2b();
    blake2b.update("".getBytes(StandardCharsets.UTF_8));
    Assertions.assertEquals(
        blake2b.doFinalString(),
        "0x44f4c69744d5f8c55d642062949dcae49bc4e7ef43d388c5a12f42b5633d163e");

    blake2b.update("The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8));
    Assertions.assertEquals(
        blake2b.doFinalString(),
        "0xabfa2c08d62f6f567d088d6ba41d3bbbb9a45c241a8e3789ef39700060b5cee2");
  }

  @Test
  public void testBlake2b() {
    Blake2b blake2b = new Blake2b();
    blake2b.update("The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8));

    Blake2b anotherBlake2b = new Blake2b();
    anotherBlake2b.update(
        "The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8));
    Assertions.assertEquals(
        blake2b.doFinalString(),
        "0xabfa2c08d62f6f567d088d6ba41d3bbbb9a45c241a8e3789ef39700060b5cee2");
  }
}
