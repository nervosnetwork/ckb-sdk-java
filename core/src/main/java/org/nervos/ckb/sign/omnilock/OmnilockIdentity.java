package org.nervos.ckb.sign.omnilock;

import java.util.List;

public class OmnilockIdentity {
  private Auth identity;
  private List<SmtProofEntry> proofs;

  public Auth getIdentity() {
    return identity;
  }

  public void setIdentity(Auth identity) {
    this.identity = identity;
  }

  public List<SmtProofEntry> getProofs() {
    return proofs;
  }

  public void setProofs(List<SmtProofEntry> proofs) {
    this.proofs = proofs;
  }

  public static class SmtProofEntry {
    private byte mask;
    private byte[] smtProof;

    public byte getMask() {
      return mask;
    }

    public void setMask(byte mask) {
      this.mask = mask;
    }

    public byte[] getSmtProof() {
      return smtProof;
    }

    public void setSmtProof(byte[] smtProof) {
      this.smtProof = smtProof;
    }
  }

  public enum OmnilockFlag {
    CKB_SECP256K1_BLAKE160((byte) 0x0),
    LOCK_SCRIPT_HASH((byte) 0xFC);
    private byte value;

    OmnilockFlag(byte value) {
      this.value = value;
    }

    public byte getValue() {
      return value;
    }

    public static OmnilockFlag valueOf(byte flag) {
      for (OmnilockFlag omnilockFlag: OmnilockFlag.values()) {
        if (omnilockFlag.value == flag) {
          return omnilockFlag;
        }
      }
      throw new IllegalArgumentException("unknown value");
    }
  }

  public class Auth {
    private OmnilockFlag flag;
    private byte[] authContent;

    public OmnilockFlag getFlag() {
      return flag;
    }

    public void setFlag(OmnilockFlag flag) {
      this.flag = flag;
    }

    public byte[] getAuthContent() {
      return authContent;
    }

    public void setAuthContent(byte[] authContent) {
      this.authContent = authContent;
    }

    public byte[] encode() {
      byte[] encoded = new byte[21];
      encoded[0] = flag.getValue();
      System.arraycopy(authContent, 0, encoded, 1, 20);
      return encoded;
    }
  }

}
