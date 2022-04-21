package org.nervos.ckb.crypto;

import org.bouncycastle.crypto.digests.Blake2bDigest;

public class Blake2b {

  private Blake2bDigest blake2bDigest;

  public Blake2b() {
    blake2bDigest = new Blake2bDigest(null, 32, null, Hash.CKB_HASH_PERSONALIZATION);
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
}
