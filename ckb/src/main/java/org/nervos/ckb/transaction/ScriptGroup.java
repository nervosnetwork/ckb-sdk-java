package org.nervos.ckb.transaction;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */

// A script group is defined as scripts that share the same hash.
// A script group will only be executed once per transaction
public class ScriptGroup {

  public List<Integer> inputIndexes;

  public ScriptGroup(List<Integer> inputIndexes) {
    this.inputIndexes = inputIndexes;
  }
}
