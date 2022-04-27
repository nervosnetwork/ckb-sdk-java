package org.nervos.ckb.utils;

import org.nervos.ckb.type.*;

import java.util.ArrayList;
import java.util.List;

public class Convert {

  public static OutPoint parseOutPoint(OutPoint outPoint) {
    return new OutPoint(outPoint.txHash, outPoint.index);
  }

  public static Transaction parseTransaction(Transaction transaction) {
    List<CellDep> cellDeps = new ArrayList<>();
    for (CellDep cellDep : transaction.cellDeps) {
      cellDeps.add(
          new CellDep(
              new OutPoint(cellDep.outPoint.txHash, cellDep.outPoint.index), cellDep.depType));
    }

    List<CellInput> inputs = new ArrayList<>();
    for (CellInput cellInput : transaction.inputs) {
      inputs.add(
          new CellInput(
              new OutPoint(cellInput.previousOutput.txHash, cellInput.previousOutput.index),
              cellInput.since));
    }

    List<CellOutput> outputs = new ArrayList<>();
    for (CellOutput cellOutput : transaction.outputs) {
      outputs.add(new CellOutput(cellOutput.capacity, cellOutput.lock, cellOutput.type));
    }

    return new Transaction(
        transaction.version,
        cellDeps,
        transaction.headerDeps,
        inputs,
        outputs,
        transaction.outputsData,
        transaction.witnesses);
  }
}
