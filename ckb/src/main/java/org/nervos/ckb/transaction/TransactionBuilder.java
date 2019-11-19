package org.nervos.ckb.transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionBuilder {

  private List<CellInput> cellInputs = new ArrayList<>();
  private List<CellOutput> cellOutputs = new ArrayList<>();
  private List<String> cellOutputsData = new ArrayList<>();
  private List<CellDep> cellDeps = new ArrayList<>();
  private List witnesses = new ArrayList<>();

  public TransactionBuilder(Api api) {
    this(api, false);
  }

  public TransactionBuilder(Api api, boolean isMultiSig) {
    try {
      if (isMultiSig) {
        this.cellDeps.add(
            new CellDep(SystemContract.getSystemMultiSigCell(api).outPoint, CellDep.DEP_GROUP));
      } else {
        this.cellDeps.add(
            new CellDep(SystemContract.getSystemSecpCell(api).outPoint, CellDep.DEP_GROUP));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addInput(CellInput input) {
    cellInputs.add(input);
  }

  public void addInputs(List<CellInput> inputs) {
    cellInputs.addAll(inputs);
  }

  public void addWitnesses(List witnesses) {
    this.witnesses = witnesses;
  }

  public void addWitness(Object witness) {
    this.witnesses.add(witness);
  }

  public void addOutput(CellOutput output) {
    cellOutputs.add(output);
  }

  public void addOutputs(List<CellOutput> outputs) {
    cellOutputs.addAll(outputs);
  }

  public void addCellDep(CellDep cellDep) {
    cellDeps.add(cellDep);
  }

  public Transaction buildTx() throws IOException {
    BigInteger needCapacity = BigInteger.ZERO;
    for (CellOutput output : cellOutputs) {
      needCapacity = needCapacity.add(Numeric.toBigInt(output.capacity));
    }
    if (cellInputs.size() == 0) {
      throw new IOException("Cell inputs could not empty");
    }
    for (int i = 0; i < cellOutputs.size(); i++) {
      cellOutputsData.add("0x");
    }

    return new Transaction(
        "0",
        cellDeps,
        Collections.emptyList(),
        cellInputs,
        cellOutputs,
        cellOutputsData,
        witnesses);
  }
}
