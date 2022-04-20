package org.nervos.indexer.model;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

import java.util.ArrayList;

public class SearchKeyBuilder {

  private Script script;

  private ScriptType scriptType;

  private Filter filter;

  public void script(Script script) {
    this.script = script;
  }

  public void scriptType(ScriptType scriptType) {
    this.scriptType = scriptType;
  }

  public void filterScript(Script script) {
    initFilter();
    this.filter.script = script;
  }

  public void filterOutputDataLenRange(int inclusive, int exclusive) {
    initFilter();
    this.filter.outputDataLenRange = new ArrayList<>(2);
    this.filter.outputDataLenRange.add(inclusive);
    this.filter.outputDataLenRange.add(exclusive);
  }

  public void filterOutputCapacityRange(long inclusive, long exclusive) {
    initFilter();
    this.filter.outputCapacityRange = new ArrayList<>(2);
    this.filter.outputCapacityRange.add(inclusive);
    this.filter.outputCapacityRange.add(exclusive);
  }

  public void filterBlockRange(int inclusive, int exclusive) {
    initFilter();
    this.filter.blockRange = new ArrayList<>(2);
    this.filter.blockRange.add(inclusive);
    this.filter.blockRange.add(exclusive);
  }

  public SearchKey build() {
    SearchKey searchKey = new SearchKey();
    searchKey.script = this.script;
    searchKey.scriptType = this.scriptType;
    searchKey.filter = this.filter;

    return searchKey;
  }

  private void initFilter() {
    if (this.filter == null) {
      this.filter = new Filter();
    }
  }
}
