package org.nervos.indexer.model;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

public class SearchKey {
  public Script script;
  public ScriptType scriptType;
  /**
   * Script search mode, optional default is prefix, means search script with prefix
   */
  public ScriptSearchMode scriptSearchMode;
  public Filter filter;
  /**
   * bool, optional default is true, if with_data is set to false, the field of returning cell.output_data is null in the result
   */
  public Boolean withData;
  public boolean groupByTransaction;
}
