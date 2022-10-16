package org.nervos.ckb.sign.omnilock;

public enum AuthFlag {
  CKB_SECP256K1_BLAKE160((byte) 0x0),
  ETHEREUM((byte) 0x1),
  EOS((byte) 0x2),
  TRON((byte) 0x3),
  BITCOIN((byte) 0x4),
  DOGECOIN((byte) 0x5),
  CKB_MULTI_SIG((byte) 0x6),
  LOCK_SCRIPT_HASH((byte) 0xFC),
  EXEC((byte) 0xFD),
  DYNAMIC_LINKING((byte) 0xFE);
  private byte value;

  AuthFlag(byte value) {
    this.value = value;
  }

  public byte getValue() {
    return value;
  }

  public static AuthFlag valueOf(byte flag) {
    for (AuthFlag authFlag: AuthFlag.values()) {
      if (authFlag.value == flag) {
        return authFlag;
      }
    }
    throw new IllegalArgumentException("unknown value");
  }
}
