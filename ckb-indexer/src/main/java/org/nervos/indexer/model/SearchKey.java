package org.nervos.indexer.model;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

public class SearchKey {
  public Script script;
  public ScriptType scriptType;
  public Filter filter;
}
