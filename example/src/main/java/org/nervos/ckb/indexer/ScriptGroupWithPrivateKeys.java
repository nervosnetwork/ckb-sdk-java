package org.nervos.ckb.indexer;

import java.util.List;
import org.nervos.ckb.transaction.ScriptGroup;

public class ScriptGroupWithPrivateKeys {

  public ScriptGroup scriptGroup;
  public List<String> privateKeys;

  public ScriptGroupWithPrivateKeys(ScriptGroup scriptGroup, List<String> privateKeys) {
    this.scriptGroup = scriptGroup;
    this.privateKeys = privateKeys;
  }
}
