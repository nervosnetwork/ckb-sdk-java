package org.nervos.ckb.type.transaction;

import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;

import java.util.List;

import static org.nervos.ckb.utils.MoleculeConverter.*;

public class RawTransaction {
    public int version;
    public List<CellDep> cellDeps;
    public List<byte[]> headerDeps;
    public List<CellInput> inputs;
    public List<CellOutput> outputs;
    public List<byte[]> outputsData;

    public org.nervos.ckb.newtype.concrete.RawTransaction pack() {
        return org.nervos.ckb.newtype.concrete.RawTransaction.builder()
                .setVersion(packUint32(version))
                .setCellDeps(packCellDepVec(cellDeps))
                .setHeaderDeps(packByte32Vec(headerDeps))
                .setInputs(packCellInputVec(inputs))
                .setOutputs(packCellOutputVec(outputs))
                .setOutputsData(packBytesVec(outputsData))
                .build();
    }
}
