package org.nervos.ckb.utils;

import org.nervos.ckb.newtype.base.Molecule;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;

import java.math.BigInteger;

public class Serializer2 {
  public static byte[] serialize(CellDep in) {
    return MoleculeFactory.createCellDep(in).toByteArray();
  }

  public static byte[] serialize(CellInput in) {
    return MoleculeFactory.createCellInput(in).toByteArray();
  }

  public static byte[] serialize(CellOutput in) {
    return MoleculeFactory.createCellOutput(in).toByteArray();
  }

  public static byte[] serialize(Transaction in, boolean includeWitnesses) {
    if (includeWitnesses) {
      return MoleculeFactory.createTransaction(in).toByteArray();
    } else {
      return MoleculeFactory.createRawTransaction(in).toByteArray();
    }
  }

  public static byte[] serialize(Header in, boolean includeNonce) {
    if (includeNonce) {
      return MoleculeFactory.createHeader(in).toByteArray();
    } else {
      return MoleculeFactory.createRawHeader(in).toByteArray();
    }
  }

  public static byte[] serialize(WitnessArgs in) {
    return MoleculeFactory.createWitnessArgs(in).toByteArray();
  }

  public static byte[] serialize(Script script) {
    return MoleculeFactory.createScript(script).toByteArray();
  }

  public static byte[] serialize(long in, MoleculeNumber type) {
    return serialize(BigInteger.valueOf(in), type);
  }

  public static byte[] serialize(BigInteger in, MoleculeNumber type) {
    Molecule molecule;
    switch (type) {
      case UINT32:
        molecule = MoleculeFactory.createUnit32(in);
        break;
      case UINT64:
        molecule = MoleculeFactory.createUnit64(in);
        break;
      case UINT128:
        molecule = MoleculeFactory.createUnit128(in);
        break;
      default:
        throw new IllegalArgumentException("Unsupported molecule number type");
    }
    return molecule.toByteArray();
  }

  public enum MoleculeNumber {
    UINT32,
    UINT64,
    UINT128
  }
}
