package org.nervos.ckb.utils;

import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Convert {

  public static OutPoint parseOutPoint(OutPoint outPoint) {
    return new OutPoint(outPoint.txHash, Numeric.toHexString(outPoint.index));
  }

  public static Transaction parseTransaction(Transaction transaction) {
    List<CellDep> cellDeps = new ArrayList<>();
    for (CellDep cellDep : transaction.cellDeps) {
      cellDeps.add(
          new CellDep(
              new OutPoint(cellDep.outPoint.txHash, Numeric.toHexString(cellDep.outPoint.index)),
              cellDep.depType));
    }

    List<CellInput> inputs = new ArrayList<>();
    for (CellInput cellInput : transaction.inputs) {
      inputs.add(
          new CellInput(
              new OutPoint(
                  cellInput.previousOutput.txHash,
                  Numeric.toHexString(cellInput.previousOutput.index)),
              Numeric.toHexString(cellInput.since)));
    }

    List<CellOutput> outputs = new ArrayList<>();
    for (CellOutput cellOutput : transaction.outputs) {
      outputs.add(
          new CellOutput(
              Numeric.toHexString(cellOutput.capacity), cellOutput.lock, cellOutput.type));
    }

    return new Transaction(
        Numeric.toHexString(transaction.version),
        cellDeps,
        transaction.headerDeps,
        inputs,
        outputs,
        transaction.outputsData,
        transaction.witnesses);
  }
}
