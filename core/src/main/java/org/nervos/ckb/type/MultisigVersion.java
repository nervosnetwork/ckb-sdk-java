package org.nervos.ckb.type;

public enum MultisigVersion {
  /// Multisig Script deployed on Genesis Block
  /// https://explorer.nervos.org/script/0x5c5069eb0857efc65e1bca0c07df34c31663b3622fd3876c876320fc9634e2a8/type
  Legacy,
  /// Latest multisig script, Enhance multisig handling for optional since value
  /// https://github.com/nervosnetwork/ckb-system-scripts/pull/99
  /// https://explorer.nervos.org/script/0x36c971b8d41fbd94aabca77dc75e826729ac98447b46f91e00796155dddb0d29/data1
  V2;

  public byte[] codeHash(){
    switch (this)
    {
      case Legacy:
        return Script.SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_LEGACY;
      case V2:
        return Script.SECP256K1_BLAKE160_MULTISIG_ALL_CODE_HASH_V2;
      default:
        throw new IllegalArgumentException("Unknown multisig version: " + this);
    }
  }
  
  public Script.HashType hashType() {
    switch (this)
    {
      case Legacy:
        return Script.HashType.TYPE;
      case V2:
        return Script.HashType.DATA1;
      default:
        throw new IllegalArgumentException("Unknown multisig version: " + this);
    }
  }
}
