package org.nervos.ckb.type;

import java.util.List;

import static org.nervos.ckb.utils.MoleculeConverter.*;

public class RawTransaction {
  public int version;
  public List<CellDep> cellDeps;
  public List<byte[]> headerDeps;
  public List<CellInput> inputs;
  public List<CellOutput> outputs;
  public List<byte[]> outputsData;

  public org.nervos.ckb.type.concrete.RawTransaction pack() {
    return org.nervos.ckb.type.concrete.RawTransaction.builder()
        .setVersion(packUint32(version))
        .setCellDeps(packCellDepVec(cellDeps))
        .setHeaderDeps(packByte32Vec(headerDeps))
        .setInputs(packCellInputVec(inputs))
        .setOutputs(packCellOutputVec(outputs))
        .setOutputsData(packBytesVec(outputsData))
        .build();
  }
}
