package org.nervos.ckb.transaction;

import java.util.List;
import org.nervos.ckb.type.cell.CellInput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellsWithPrivateKey {
  public List<CellInput> inputs;
  public String privateKey;

  public CellsWithPrivateKey(List<CellInput> inputs, String privateKey) {
    this.inputs = inputs;
    this.privateKey = privateKey;
  }
}
