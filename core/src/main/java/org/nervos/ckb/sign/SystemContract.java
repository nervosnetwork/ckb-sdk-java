package org.nervos.ckb.sign;

import org.nervos.ckb.type.CellDep;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.WitnessArgs;

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

  public enum Type implements WitnessPlaceholder {
    SECP256K1_BLAKE160_SIGHASH_ALL {
      @Override
      public byte[] getWitnessPlaceHolder(byte[] originalWitness, Object context) {
        return setWitnessLock(originalWitness, new byte[65]);
      }
    },
    SECP256K1_BLAKE160_MULTISIG_ALL {
      @Override
      public byte[] getWitnessPlaceHolder(byte[] originalWitness, Object context) {
        // TODO: to implement
        return new byte[0];
      }
    },
    ANYONE_CAN_PAY {
      @Override
      public byte[] getWitnessPlaceHolder(byte[] originalWitness, Object context) {
        boolean toLock = (boolean) context;
        if (toLock) {
          return setWitnessLock(originalWitness, new byte[65]);
        } else {
          return originalWitness;
        }
      }
    },
    SUDT,
    DAO;

    private static byte[] setWitnessLock(byte[] originalWitness, byte[] lock) {
      WitnessArgs witnessArgs;
      if (originalWitness == null | originalWitness.length == 0) {
        witnessArgs = new WitnessArgs();
      } else {
        witnessArgs = WitnessArgs.unpack(originalWitness);
      }
      witnessArgs.setLock(lock);
      return witnessArgs.pack().toByteArray();
    }
  }
}
