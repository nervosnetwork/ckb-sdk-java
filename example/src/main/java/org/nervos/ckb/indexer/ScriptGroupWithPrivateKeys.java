package org.nervos.ckb.indexer;

import org.nervos.ckb.transaction.ScriptGroup;

import java.util.List;

public class ScriptGroupWithPrivateKeys {

  public ScriptGroup scriptGroup;
  public List<String> privateKeys;

  public ScriptGroupWithPrivateKeys(ScriptGroup scriptGroup, List<String> privateKeys) {
    this.scriptGroup = scriptGroup;
    this.privateKeys = privateKeys;
  }
}
