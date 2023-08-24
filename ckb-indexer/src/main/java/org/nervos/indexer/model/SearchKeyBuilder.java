package org.nervos.indexer.model;

import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;

import java.util.ArrayList;

public class SearchKeyBuilder {

  private Script script;

  private ScriptType scriptType;

  private Filter filter;

  public SearchKeyBuilder script(Script script) {
    this.script = script;
    return this;
  }

  public SearchKeyBuilder scriptType(ScriptType scriptType) {
    this.scriptType = scriptType;
    return this;
  }

  public SearchKeyBuilder filterScript(Script script) {
    initFilter();
    this.filter.script = script;
    return this;
  }

  public SearchKeyBuilder filterOutputDataLenRange(int inclusive, int exclusive) {
    initFilter();
    this.filter.outputDataLenRange = new ArrayList<>(2);
    this.filter.outputDataLenRange.add(inclusive);
    this.filter.outputDataLenRange.add(exclusive);
    return this;
  }

  public SearchKeyBuilder filterOutputCapacityRange(long inclusive, long exclusive) {
    initFilter();
    this.filter.outputCapacityRange = new ArrayList<>(2);
    this.filter.outputCapacityRange.add(inclusive);
    this.filter.outputCapacityRange.add(exclusive);
    return this;
  }

  public SearchKeyBuilder filterBlockRange(int inclusive, int exclusive) {
    initFilter();
    this.filter.blockRange = new ArrayList<>(2);
    this.filter.blockRange.add(inclusive);
    this.filter.blockRange.add(exclusive);
    return this;
  }

  public SearchKeyBuilder filterScriptLen(int inclusive, int exclusive) {
    initFilter();
    this.filter.scriptLenRange = new ArrayList<>(2);
    this.filter.scriptLenRange.add(inclusive);
    this.filter.scriptLenRange.add(exclusive);
    return this;
  }

  private ScriptSearchMode _scriptSearchMode;

  public SearchKeyBuilder scriptSearchMode(ScriptSearchMode scriptSearchMode) {
    this._scriptSearchMode = scriptSearchMode;
    return this;
  }

  private Boolean _withData;

  public SearchKeyBuilder withData(Boolean withData) {
    this._withData = withData;
    return this;
  }

  public SearchKey build() {
    SearchKey searchKey = new SearchKey();
    searchKey.script = this.script;
    searchKey.scriptType = this.scriptType;
    searchKey.filter = this.filter;
    searchKey.scriptSearchMode = this._scriptSearchMode;
    searchKey.withData = this._withData;
    // searchKey.groupByTransaction controlled by api function

    return searchKey;
  }

  private void initFilter() {
    if (this.filter == null) {
      this.filter = new Filter();
    }
  }
}
