package org.nervos.ckb.sign;

import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.Script;

import java.util.List;

public class SystemContract {
  private byte[] codeHash;
  private Script.HashType hashType = Script.HashType.TYPE;
  private List<CellDep> cellDeps;

  public SystemContract(byte[] codeHash, Script.HashType hashType, List<CellDep> cellDeps) {
    this.codeHash = codeHash;
    this.hashType = hashType;
    this.cellDeps = cellDeps;
  }

  public byte[] getCodeHash() {
    return codeHash;
  }

  public Script.HashType getHashType() {
    return hashType;
  }

  public List<CellDep> getCellDeps() {
    return cellDeps;
  }

  public Script createScript(byte[] args) {
    return new Script(codeHash, args, hashType);
  }

  public enum Type {
    SECP256K1_BLAKE160_SIGHASH_ALL,
    SECP256K1_BLAKE160_MULTISIG_ALL,
    ANYONE_CAN_PAY,
    SUDT,
    DAO,
  }
}
