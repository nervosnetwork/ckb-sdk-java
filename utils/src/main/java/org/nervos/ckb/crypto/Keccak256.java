package org.nervos.ckb.crypto;

import org.bouncycastle.jcajce.provider.digest.Keccak;

public class Keccak256 {

  private Keccak.Digest256 digest256;

  public Keccak256() {
    digest256 = new Keccak.Digest256();
  }

  public void update(byte[] input) {
    if (digest256 != null) {
      digest256.update(input, 0, input.length);
    }
  }

  public byte[] doFinalBytes() {
    byte[] out = new byte[32];
    if (digest256 != null) {
      out = digest256.digest();
    }
    return out;
  }
}
