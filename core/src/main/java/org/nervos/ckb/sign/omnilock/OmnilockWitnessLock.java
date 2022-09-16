package org.nervos.ckb.sign.omnilock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.nervos.ckb.utils.MoleculeConverter.packBytes;

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

  public org.nervos.ckb.sign.omnilock.molecule.OmniLockWitnessLock pack() {
    org.nervos.ckb.sign.omnilock.molecule.OmniLockWitnessLock.Builder moleculeLock = org.nervos.ckb.sign.omnilock.molecule.OmniLockWitnessLock.builder();
    if (signature != null) {
      moleculeLock.setSignature(packBytes(signature));
    }
    if (preimage != null) {
      moleculeLock.setPreimage(packBytes(preimage));
    }
    if (omnilockIdentity != null) {
      org.nervos.ckb.sign.omnilock.molecule.Identity.Builder identityBuilder = org.nervos.ckb.sign.omnilock.molecule.Identity.builder();
      org.nervos.ckb.sign.omnilock.molecule.Auth.Builder authBuilder = org.nervos.ckb.sign.omnilock.molecule.Auth.builder(omnilockIdentity.getIdentity().encode());
      identityBuilder.setIdentity(authBuilder.build());
      org.nervos.ckb.sign.omnilock.molecule.SmtProofEntryVec.Builder smtProofEntryVec = org.nervos.ckb.sign.omnilock.molecule.SmtProofEntryVec.builder();
      for (OmnilockIdentity.SmtProofEntry s: omnilockIdentity.getProofs()) {
        smtProofEntryVec.add(packSmtProofEntry(s));
      }
      identityBuilder.setProofs(smtProofEntryVec.build());
      moleculeLock.setOmniIdentity(identityBuilder.build());
    }
    return moleculeLock.build();
  }

  public static OmnilockWitnessLock unpack(byte[] in) {
    if (in == null) {
      return null;
    }
    org.nervos.ckb.sign.omnilock.molecule.OmniLockWitnessLock moleculeOmniLockWitnessLock = org.nervos.ckb.sign.omnilock.molecule.OmniLockWitnessLock.builder(in).build();
    OmnilockWitnessLock omnilockWitnessLock = new OmnilockWitnessLock();
    if (moleculeOmniLockWitnessLock.getPreimage() != null) {
      omnilockWitnessLock.setPreimage(moleculeOmniLockWitnessLock.getPreimage().toByteArray());
    }
    if (moleculeOmniLockWitnessLock.getSignature() != null) {
      omnilockWitnessLock.setSignature(moleculeOmniLockWitnessLock.getSignature().getItems());
    }

    if (moleculeOmniLockWitnessLock.getOmniIdentity() != null) {
      OmnilockIdentity identity = new OmnilockIdentity();
      List<OmnilockIdentity.SmtProofEntry> proofs = new ArrayList<>();
      org.nervos.ckb.sign.omnilock.molecule.Identity moleculeIdentity = moleculeOmniLockWitnessLock.getOmniIdentity();
      for (int i = 0; i < moleculeIdentity.getProofs().getSize(); i++) {
        org.nervos.ckb.sign.omnilock.molecule.SmtProofEntry moleculeSmtProofEntry = moleculeIdentity.getProofs().get(i);
        OmnilockIdentity.SmtProofEntry smtProofEntry = new OmnilockIdentity.SmtProofEntry();
        smtProofEntry.setMask(moleculeSmtProofEntry.getMask());
        smtProofEntry.setSmtProof(moleculeSmtProofEntry.getProof().toByteArray());
        proofs.add(smtProofEntry);
      }

      byte[] bytes = moleculeIdentity.getIdentity().toByteArray();
      omnilockWitnessLock.setOmnilockIdentity(identity);
      OmnilockIdentity.Auth auth = new OmnilockIdentity.Auth();
      auth.setFlag(OmnilockIdentity.OmnilockFlag.valueOf(bytes[0]));
      auth.setAuthContent(Arrays.copyOfRange(bytes, 1, 20));

      identity.setIdentity(auth);
      identity.setProofs(proofs);
      omnilockWitnessLock.setOmnilockIdentity(identity);
    }

    return omnilockWitnessLock;
  }

  public org.nervos.ckb.sign.omnilock.molecule.SmtProofEntry packSmtProofEntry(OmnilockIdentity.SmtProofEntry smtProofEntry) {
    org.nervos.ckb.sign.omnilock.molecule.SmtProofEntry.Builder builder = org.nervos.ckb.sign.omnilock.molecule.SmtProofEntry.builder();
    builder.setProof(org.nervos.ckb.sign.omnilock.molecule.SmtProof.builder(smtProofEntry.getSmtProof()).build());
    builder.setMask(smtProofEntry.getMask());
    return builder.build();
  }
}
