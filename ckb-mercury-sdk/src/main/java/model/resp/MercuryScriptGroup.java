package model.resp;

import org.nervos.ckb.transaction.ScriptGroup;

import java.util.List;

public class MercuryScriptGroup extends ScriptGroup {

  public String pubKey;

  public MercuryScriptGroup(String pubKey, List<Integer> inputIndexes) {
    super(inputIndexes);
    this.pubKey = pubKey;
  }
}
