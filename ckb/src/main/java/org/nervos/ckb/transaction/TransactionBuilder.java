package org.nervos.ckb.transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.system.SystemContract;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.transaction.Transaction;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class TransactionBuilder {

  private List<CellInput> cellInputs = new ArrayList<>();
  private List<CellOutput> cellOutputs = new ArrayList<>();
  private List<String> cellOutputsData = new ArrayList<>();
  private List<CellDep> cellDeps = new ArrayList<>();
  private List<String> headerDeps = Collections.emptyList();
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
    this.cellInputs.add(input);
  }

  public void addInputs(List<CellInput> inputs) {
    this.cellInputs.addAll(inputs);
  }

  public void setInputs(List<CellInput> inputs) {
    this.cellInputs = inputs;
  }

  public void addWitnesses(List witnesses) {
    this.witnesses = witnesses;
  }

  public void addWitness(Object witness) {
    this.witnesses.add(witness);
  }

  public void addOutput(CellOutput output) {
    this.cellOutputs.add(output);
  }

  public void addOutputs(List<CellOutput> outputs) {
    this.cellOutputs.addAll(outputs);
  }

  public void setOutputs(List<CellOutput> outputs) {
    this.cellOutputs = outputs;
  }

  public void addCellDep(CellDep cellDep) {
    this.cellDeps.add(cellDep);
  }

  public void addCellDeps(List<CellDep> cellDeps) {
    this.cellDeps.addAll(cellDeps);
  }

  public List<CellDep> getCellDeps() {
    return this.cellDeps;
  }

  public void setOutputsData(List<String> outputsData) {
    this.cellOutputsData = outputsData;
  }

  public void setHeaderDeps(List<String> headerDeps) {
    this.headerDeps = headerDeps;
  }

  public Transaction buildTx() {
    if (cellOutputsData.size() == 0) {
      for (int i = 0; i < cellOutputs.size(); i++) {
        cellOutputsData.add("0x");
      }
    }

    return new Transaction(
        "0", cellDeps, headerDeps, cellInputs, cellOutputs, cellOutputsData, witnesses);
  }
}
