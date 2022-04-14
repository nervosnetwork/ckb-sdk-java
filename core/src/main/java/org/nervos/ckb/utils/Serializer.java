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

public class Serializer {
  public static byte[] serialize(CellDep in) {
    return in.pack().toByteArray();
  }

  public static byte[] serialize(CellInput in) {
    return in.pack().toByteArray();
  }

  public static byte[] serialize(CellOutput in) {
    return in.pack().toByteArray();
  }

  public static byte[] serialize(Transaction in, boolean includeWitnesses) {
    if (includeWitnesses) {
      return in.pack().toByteArray();
    } else {
      return in.getRawTransaction().pack().toByteArray();
    }
  }

  public static byte[] serialize(Header in, boolean includeNonce) {
    if (includeNonce) {
      return in.pack().toByteArray();
    } else {
      return in.getRawHeader().pack().toByteArray();
    }
  }

  public static byte[] serialize(WitnessArgs in) {
    return in.pack().toByteArray();
  }

  public static byte[] serialize(Script script) {
    return script.pack().toByteArray();
  }

  public static byte[] serialize(long in, MoleculeNumber type) {
    return serialize(BigInteger.valueOf(in), type);
  }

  public static byte[] serialize(BigInteger in, MoleculeNumber type) {
    Molecule molecule;
    switch (type) {
      case UINT32:
        molecule = MoleculeConverter.packUint32(in);
        break;
      case UINT64:
        molecule = MoleculeConverter.packUint64(in);
        break;
      case UINT128:
        molecule = MoleculeConverter.packUint128(in);
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
