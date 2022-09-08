package org.nervos.ckb.sign.omnilock;

import org.nervos.ckb.type.base.Molecule;

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

  public OmnilockWitnessLockMolecule pack() {
    // TODO: complete
    return new OmnilockWitnessLockMolecule();
  }

  public static OmnilockWitnessLock unpack(byte[] in) {
    // TODO: complete
    return new OmnilockWitnessLock();
  }
 
  // Temporary class
  public static class OmnilockWitnessLockMolecule extends Molecule {
  }
}
