package org.nervos.ckb.transaction;

import java.util.List;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class ScriptGroupWithPrivateKeys {

  public ScriptGroup scriptGroup;
  public List<String> privateKeys;

  public ScriptGroupWithPrivateKeys(ScriptGroup scriptGroup, List<String> privateKeys) {
    this.scriptGroup = scriptGroup;
    this.privateKeys = privateKeys;
  }
}
