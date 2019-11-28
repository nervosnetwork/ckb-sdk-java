package org.nervos.ckb.transaction;

import java.util.ArrayList;
import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */

// A script group is defined as scripts that share the same hash.
// A script group will only be executed once per transaction
public class ScriptGroup {

  public List<Integer> inputIndices;

  public ScriptGroup() {
    inputIndices = new ArrayList<>();
  }

  public ScriptGroup(List<Integer> inputIndexes) {
    this.inputIndices = inputIndexes;
  }

  public void addIndex(int index) {
    this.inputIndices.add(index);
  }

  public void addIndices(List<Integer> inputIndices) {
    this.inputIndices.addAll(inputIndices);
  }
}
