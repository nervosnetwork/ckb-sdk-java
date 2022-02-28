package org.nervos.ckb.unlocker;

// Todo: there is another org.nervos.indexer.model.ScriptType. To avoid circular dependency I create
// a new one. Need refactor.
public enum ScriptType {
  LOCK,
  TYPE
}
