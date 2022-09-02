package org.nervos.ckb.sign.omnilock;

public class OmnilockWitnessLock {
  private byte[] signature;
  private OmnilockIdentity omnilockIdentity;
  private byte[] preimage;

  public byte[] getSignature() {
    return signature;
  }

  public void setSignature(byte[] signature) {
    this.signature = signature;
  }

  public OmnilockIdentity getOmnilockIdentity() {
    return omnilockIdentity;
  }

  public void setOmnilockIdentity(OmnilockIdentity omnilockIdentity) {
    this.omnilockIdentity = omnilockIdentity;
  }

  public byte[] getPreimage() {
    return preimage;
  }

  public void setPreimage(byte[] preimage) {
    this.preimage = preimage;
  }
}
