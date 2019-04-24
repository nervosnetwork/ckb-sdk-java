package org.nervos.ckb.methods.type;

import java.util.List;

/** Created by duanyytop on 2018-12-21. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Transaction {

  public int version;
  public String hash;
  public List<OutPoint> deps;
  public List<CellInput> inputs;
  public List<CellOutput> outputs;
  public List<Witness> witnesses;

  public Transaction() {}

  public Transaction(
      int version,
      List<OutPoint> deps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<Witness> witnesses) {
    this.version = version;
    this.deps = deps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.witnesses = witnesses;
  }
}
