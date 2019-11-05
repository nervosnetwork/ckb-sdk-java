package org.nervos.ckb.transaction;

import java.util.Collections;
import java.util.List;
import org.nervos.ckb.type.cell.CellInput;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class CellsWithPrivateKeys {
  public List<CellInput> inputs;
  public List<String> privateKeys;

  public CellsWithPrivateKeys(List<CellInput> inputs, String privateKey) {
    this.inputs = inputs;
    this.privateKeys = Collections.singletonList(privateKey);
  }

  public CellsWithPrivateKeys(List<CellInput> inputs, List<String> privateKeys) {
    this.inputs = inputs;
    this.privateKeys = privateKeys;
  }
}
