package org.nervos.mercury.model.resp;

import java.util.List;
import org.nervos.ckb.transaction.ScriptGroup;

public class MercuryScriptGroup extends ScriptGroup {

  public String pubKey;

  public MercuryScriptGroup(String pubKey, List<Integer> inputIndexes) {
    super(inputIndexes);
    this.pubKey = pubKey;
  }
}
