package org.nervos.ckb.crypto;

import org.bouncycastle.crypto.digests.Blake2bDigest;

import java.nio.charset.StandardCharsets;

public class Blake2b {
  protected static final byte[] CKB_HASH_PERSONALIZATION =
      "ckb-default-hash".getBytes(StandardCharsets.UTF_8);
  private Blake2bDigest blake2bDigest;

  public Blake2b() {
    blake2bDigest = new Blake2bDigest(null, 32, null, CKB_HASH_PERSONALIZATION);
  }

  public void update(byte[] input) {
    if (blake2bDigest != null) {
      blake2bDigest.update(input, 0, input.length);
    }
  }

  public byte[] doFinalBytes() {
    byte[] out = new byte[32];
    if (blake2bDigest != null) {
      blake2bDigest.doFinal(out, 0);
    }
    return out;
  }

  public static byte[] digest(byte[] input) {
    Blake2b blake2b = new Blake2b();
    blake2b.update(input);
    return blake2b.doFinalBytes();
  }
}
