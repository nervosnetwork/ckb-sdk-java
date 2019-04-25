package org.nervos.ckb.crypto;

import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.nervos.ckb.utils.Numeric;

import java.nio.charset.StandardCharsets;

/** Cryptographic hash functions. */
public class Hash {
  protected static final byte[] CKB_HASH_PERSONALIZATION =
      "ckb-default-hash".getBytes(StandardCharsets.UTF_8);

  private Hash() {}

  /**
   * Blake2b-256 hash function.
   *
   * @param hexInput hex encoded input data with optional 0x prefix
   * @return hash value as hex encoded string
   */
  public static String blake2b(String hexInput) {
    byte[] bytes = Numeric.hexStringToByteArray(hexInput);
    byte[] result = blake2b(bytes);
    return Numeric.toHexString(result);
  }
  /**
   * Blake2b-256 hash function.
   *
   * @param input binary encoded input data
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

  /**
   * Blake2b-256 hash function that operates on a UTF-8 encoded String.
   *
   * @param utf8String UTF-8 encoded string
   * @return hash value as hex encoded string
   */
  public static String blake2bString(String utf8String) {
    return Numeric.toHexString(blake2b(utf8String.getBytes(StandardCharsets.UTF_8)));
  }
}
