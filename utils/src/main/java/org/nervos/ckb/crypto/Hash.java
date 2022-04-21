package org.nervos.ckb.crypto;

import org.bouncycastle.crypto.digests.Blake2bDigest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/** Cryptographic hash functions. */
public class Hash {
  protected static final byte[] CKB_HASH_PERSONALIZATION =
      "ckb-default-hash".getBytes(StandardCharsets.UTF_8);

  private Hash() {
  }

  /**
   * Blake2b-256 hash function.
   *
   * @param input  binary encoded input data
   * @param offset of start of data
   * @param length of data
   * @return hash value
   */
  public static byte[] blake2b(byte[] input, int offset, int length) {
    Blake2bDigest blake2b = new Blake2bDigest(null, 32, null, CKB_HASH_PERSONALIZATION);
    blake2b.update(input, offset, length);
    byte[] out = new byte[32];
    blake2b.doFinal(out, 0);
    return out;
  }

  /**
   * Blake2b-256 hash function.
   *
   * @param input binary encoded input data
   * @return hash value
   */
  public static byte[] blake2b(byte[] input) {
    return blake2b(input, 0, input.length);
  }

  public static byte[] blake160(byte[] input) {
    byte[] hash = blake2b(input);
    return Arrays.copyOfRange(hash, 0, 20);
  }
}
