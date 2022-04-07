package org.nervos.ckb.utils;

import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.WitnessArgs;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;

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
}
